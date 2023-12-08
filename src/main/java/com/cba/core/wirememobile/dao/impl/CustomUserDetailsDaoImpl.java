package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.CustomUserDetailsDao;
import com.cba.core.wirememobile.dto.ApplicationUserDto;
import com.cba.core.wirememobile.dto.ChangePasswordRequestDto;
import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
import com.cba.core.wirememobile.exception.AppSignAuthException;
import com.cba.core.wirememobile.exception.DeviceAuthException;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.*;
import com.cba.core.wirememobile.repository.AppSignatureRepository;
import com.cba.core.wirememobile.repository.DeviceConfigRepository;
import com.cba.core.wirememobile.repository.OtpRepository;
import com.cba.core.wirememobile.repository.UserRepository;
import com.cba.core.wirememobile.service.EmailService;
import com.cba.core.wirememobile.util.DeviceTypeEnum;
import com.cba.core.wirememobile.util.StatusVarList;
import com.cba.core.wirememobile.util.UserBeanUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
//@Transactional // has to add in order to overcome lazy loading issue and auto transaction mgt
@RequiredArgsConstructor
public class CustomUserDetailsDaoImpl implements CustomUserDetailsDao {

    private final UserRepository userRepository;
    private final AppSignatureRepository appSignatureRepository;
    private final DeviceConfigRepository deviceConfigRepository;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserBeanUtil userBeanUtil;
    private final EmailService emailService;
//    private final JdbcTemplate jdbcTemplate;
//    private final TransactionTemplate transactionTemplate;

    @Value("${application.otp.expireAfterMinutes}")
    private int otpExpireAfterMinutes;

    @Value("${application.login.maximumInvalidAttemptCount}")
    private int maximumInvalidAttemptCount;


    @Override
    @Transactional
    public ApplicationUserDto loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            UserType userType = new UserType();
            userType.setId(DeviceTypeEnum.MPOS.getValue()); // only web users are allowed in this module

            User user = userRepository.findByUserNameAndUserType(userName, userType).orElseThrow(() -> new NotFoundException("User not found"));

            if(user.getStatus().getStatusCode().equals("ACTV")) {
                user.setLoginAttempt(user.getLoginAttempt() + 1);
                userRepository.save(user);
            }

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

    /*
    This method excluded from @transactional for committing updated while sending custom exceptions
     */
    @Override
    public boolean validateUserDevice(UsernameAndPasswordAuthenticationRequestDto userDto) throws DeviceAuthException, AppSignAuthException {

        UserType userType = new UserType();
        userType.setId(DeviceTypeEnum.MPOS.getValue());

        User user = userRepository.findByUserNameAndUserTypeAndDevice_SerialNo(userDto.getUsername(), userType,
                userDto.getDeviceSerial())
                .orElseThrow(() -> new DeviceAuthException("No device found with given serial"));


        /*
         need to check whether the attempt count has exceeded, if true account should be blocked,
         there should be an option to reset such accounts
         */
        if (user.getLoginAttempt() > maximumInvalidAttemptCount) {
//            extracted(userDto.getUsername());
            user.setStatus(new Status("DACT"));
            userRepository.saveAndFlush(user);
            throw new DeviceAuthException("Maximum Invalid login attempt count has been exceeded.");
        } else if (!"MPOS".equalsIgnoreCase(user.getDevice().getDeviceType())) {
            throw new DeviceAuthException("Not a mobile POS device");
        } else if (!StatusVarList.ACTIVE_STATUS_CODE.equals(user.getDevice().getStatus().getStatusCode())) {
            throw new DeviceAuthException("The device is in de-activated state");
        } else if (!userDto.getUuid().equals(user.getDevice().getUniqueId())) {
            throw new DeviceAuthException("UUID has been changed since the initial login , Please contact the bank");
        } else if (!StatusVarList.ACTIVE_STATUS_CODE.equals(user.getStatus().getStatusCode())) {
            throw new DeviceAuthException("Your account is locked , Please contact the bank to unlock the account");

            /*
             here previously email has been sent, need to check
             */
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

        if (!StatusVarList.ACTIVE_STATUS_CODE.equals(deviceConfig.getStatus().getStatusCode())) {
            throw new DeviceAuthException("No active profile configuration found for this device");
        }

        if (user.getFirstLogin() == 0) {
            OnetimePassword onetimePassword = new OnetimePassword();

            onetimePassword.setUser(user);

            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime newDateTime = currentDateTime.plus(otpExpireAfterMinutes, ChronoUnit.MINUTES);
            Timestamp sqlTimestamp = Timestamp.valueOf(newDateTime);
            onetimePassword.setExpireson(sqlTimestamp);

            String otp = StringUtils.leftPad(RandomStringUtils.randomNumeric(6), 6, '0');
            onetimePassword.setValue(passwordEncoder.encode(otp));

            String message = "Your OTP for Wireme is : " + otp;
            try {
                emailService.sendEmail(user.getEmail(), message);
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new DeviceAuthException("There is an issue in sending emails");
            }
            otpRepository.save(onetimePassword);
            /*
            This is the first time login to the device, should send the email,OTP validation
            there should be an option to send and verify OTP
            OTP can be save either in the redis cache or in the Database by hashing
             */

        } else {
            /*
             need to update last login time, login attempt count reset
             */
            user.setLoginAttempt(0);
            user.setLastLoginTime(new Date());
            userRepository.save(user);

        }
        return true;
    }

//    private void extracted(String userName) {
//
//        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(TransactionStatus status) {
//                try {
//                    String sql = "UPDATE user u SET u.status = ? WHERE u.user_name = ?";
//                    int rowsAffected = jdbcTemplate.update(sql, "DACT", userName);
//                } catch (Exception ee) {
//                    status.setRollbackOnly();
//                }
//            }
//        });
//
//    }

    @Override
    @Transactional
    public boolean validateOTP(String otp) throws Exception {

        UserType userType = new UserType();
        userType.setId(DeviceTypeEnum.MPOS.getValue());

        User user = userRepository.findByUserNameAndUserType(userBeanUtil.getUsername(), userType)
                .orElseThrow(() -> new DeviceAuthException("No User Found"));

        OnetimePassword onetimePassword = otpRepository.findByUser(user).orElseThrow(() -> new NotFoundException("No OTP found"));
        String storedOtp = onetimePassword.getValue();

        if (passwordEncoder.matches(otp, storedOtp) && onetimePassword.getExpireson().compareTo(new Date()) > 0) {
            user.setFirstLogin(1);
            userRepository.save(user);
            otpRepository.deleteById(onetimePassword.getId());
            return true;
        } else {
            return false;
        }
    }

    /*
    This is also should be logged as the login attempt
     */
    @Override
    @Transactional
    public String changePassword(ChangePasswordRequestDto requestDto) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();

            UserType userType = new UserType();
            userType.setId(DeviceTypeEnum.MPOS.getValue());

            User entity = userRepository.findByUserNameAndUserType(userBeanUtil.getUsername(), userType).orElseThrow(() -> new NotFoundException("User not found"));

            if (passwordEncoder.matches(requestDto.getCurrentPassword(), entity.getPassword())) {
                entity.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
                userRepository.saveAndFlush(entity);
            } else {
                throw new NotFoundException("Fail - Old Password mismatch");
            }

//            map.put("password", "xxxxxxxx");
//            String maskValue = objectMapper.writeValueAsString(map);

            return "success";

        } catch (Exception rr) {
            throw rr;
        }
    }

    private SimpleGrantedAuthority convertToSimpleGrant(UserRole userrole) {
        return new SimpleGrantedAuthority("ROLE_" + userrole.getRole().getRoleName());
    }

}
