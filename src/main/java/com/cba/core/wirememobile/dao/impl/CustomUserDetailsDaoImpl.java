package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.CustomUserDetailsDao;
import com.cba.core.wirememobile.dto.ApplicationUserDto;
import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
import com.cba.core.wirememobile.exception.AppSignAuthException;
import com.cba.core.wirememobile.exception.DeviceAuthException;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.*;
import com.cba.core.wirememobile.repository.AppSignatureRepository;
import com.cba.core.wirememobile.repository.DeviceConfigRepository;
import com.cba.core.wirememobile.repository.DeviceRepository;
import com.cba.core.wirememobile.repository.UserRepository;
import com.cba.core.wirememobile.util.DeviceTypeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional // has to add in order to overcome lazy loading issue
@RequiredArgsConstructor
public class CustomUserDetailsDaoImpl implements CustomUserDetailsDao {

    private final UserRepository userRepository;
    //    private final DeviceRepository deviceRepository;
    private final AppSignatureRepository appSignatureRepository;
    private final DeviceConfigRepository deviceConfigRepository;

    @Override
    public ApplicationUserDto loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            UserType userType = new UserType();
            userType.setId(DeviceTypeEnum.MPOS.getValue()); // only web users are allowed in this module

            User user = userRepository.findByUserNameAndUserType(userName, userType).orElseThrow(() -> new NotFoundException("User not found"));

            Set<SimpleGrantedAuthority> permissions = user.getUserRolesForUserId()
                    .stream()
                    .map(this::convertToSimpleGrant)
                    .collect(Collectors.toSet());

            return new ApplicationUserDto(user.getUserName(), user.getPassword(), permissions,
                    true, true, true, true);

        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found cause is : " + e.getMessage());
        }

    }

    @Override
    public boolean validateUserDevice(UsernameAndPasswordAuthenticationRequestDto userDto) throws DeviceAuthException, AppSignAuthException {

//        Device device = deviceRepository.findBySerialNo(userDto.getDeviceSerial())
//                .orElseThrow(() -> new DeviceAuthException("No device found with given serial"));
        UserType userType = new UserType();
        userType.setId(DeviceTypeEnum.MPOS.getValue());

        User user = userRepository.findByUserNameAndUserTypeAndDevice_SerialNo(userDto.getUsername(), userType,
                userDto.getDeviceSerial())
                .orElseThrow(() -> new DeviceAuthException("No device found with given serial"));

        if (!"MPOS".equalsIgnoreCase(user.getDevice().getDeviceType())) {
            throw new DeviceAuthException("Not a mobile POS device");
        } else if (!"ACTV".equals(user.getDevice().getStatus().getStatusCode())) {
            throw new DeviceAuthException("The device is in de-activated state");
        } else if (!userDto.getUuid().equals(user.getDevice().getUniqueId())) {
            throw new DeviceAuthException("UUID has been changed since the initial login , Please contact the bank");
        } else if (!"ACTV".equals(user.getStatus().getStatusCode())) {
            throw new DeviceAuthException("Your account is locked , Please contact the bank to unlock the account");

            // here previously email has been sent, need to check
        }

        ApplicationSignature applicationSignature = appSignatureRepository.findByAppVersion(userDto.getAppVersion())
                .orElseThrow(() -> new AppSignAuthException("Application Signature not found"));

        if (!applicationSignature.getAppSignature().equals(userDto.getSignature())) {
            throw new AppSignAuthException("Tamper detected");
        }
        if (applicationSignature.getSysfileSize() != userDto.getSysFileSize()) {
            throw new AppSignAuthException("Tamper detected");
        }

        DeviceConfig deviceConfig = deviceConfigRepository.findByDevice_Id(user.getDevice().getId()).
                orElseThrow(() -> new DeviceAuthException("No profile configuration found for this device"));

        if (!"ACTV".equals(deviceConfig.getStatus().getStatusCode())) {
            throw new DeviceAuthException("No active profile configuration found for this device");
        }

        if(user.getFirstLogin() == 0){
            //This is the first time login to the device, should send the email,OTP validation

        }
        return true;
    }

    private SimpleGrantedAuthority convertToSimpleGrant(UserRole userrole) {
        return new SimpleGrantedAuthority("ROLE_" + userrole.getRole().getRoleName());
    }

}
