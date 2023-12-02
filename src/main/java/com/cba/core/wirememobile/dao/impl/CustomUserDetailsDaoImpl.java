package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.CustomUserDetailsDao;
import com.cba.core.wirememobile.dto.ApplicationUserDto;
import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.*;
import com.cba.core.wirememobile.repository.AppSignatureRepository;
import com.cba.core.wirememobile.repository.DeviceRepository;
import com.cba.core.wirememobile.repository.UserRepository;
import com.cba.core.wirememobile.util.UserTypeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional // has to add in order to overcome lazy loading issue
@RequiredArgsConstructor
public class CustomUserDetailsDaoImpl implements CustomUserDetailsDao {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final AppSignatureRepository appSignatureRepository;

    @Override
    public ApplicationUserDto loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            UserType userType = new UserType();
            userType.setId(UserTypeEnum.MPOS.getValue()); // only web users are allowed in this module

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
    public boolean validateUserDevice(UsernameAndPasswordAuthenticationRequestDto userDto) throws IOException {
        try {
            Device device = deviceRepository.findBySerialNo(userDto.getDeviceSerial()).orElseThrow(() -> new NotFoundException("Device not found"));
            if (!"MPOS".equals(device.getDeviceType().toUpperCase())) {

            } else {

            }

            ApplicationSignature applicationSignature = appSignatureRepository.findByAppVersion(userDto.getAppVersion()).orElseThrow(() -> new NotFoundException("Application Signature Record not found"));

            if (applicationSignature.getAppSignature() != userDto.getSignature()) {

            }
            if (applicationSignature.getSysfileSize() != userDto.getSysFileSize()) {

            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    private SimpleGrantedAuthority convertToSimpleGrant(UserRole userrole) {
        return new SimpleGrantedAuthority("ROLE_" + userrole.getRole().getRoleName());
    }

}
