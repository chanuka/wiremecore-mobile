package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.*;
import com.cba.core.wirememobile.dto.*;
import com.cba.core.wirememobile.exception.AppSignAuthException;
import com.cba.core.wirememobile.exception.DeviceAuthException;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.*;
import com.cba.core.wirememobile.service.CustomUserDetailsService;
import com.cba.core.wirememobile.service.EmailService;
import com.cba.core.wirememobile.util.DeviceTypeEnum;
import com.cba.core.wirememobile.util.StatusVarList;
import com.cba.core.wirememobile.util.UserBeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final CustomUserDetailsDao dao;
    private final UserDao userDao;
    private final OtpDao otpDao;
    private final UserBeanUtil userBeanUtil;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final AppSignatureDao appSignatureDao;
    private final DeviceConfigDao deviceConfigDao;


    @Value("${application.otp.expireAfterMinutes}")
    private int otpExpireAfterMinutes;

    @Value("${application.login.maximumInvalidAttemptCount}")
    private int maximumInvalidAttemptCount;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        try {
            UserType userType = new UserType();
            userType.setId(DeviceTypeEnum.MPOS.getValue()); // only web users are allowed in this module

            User user = dao.loadUserByUsername(userName, userType);

            if (user.getStatus().getStatusCode().equals("ACTV")) {
                user.setLoginAttempt(user.getLoginAttempt() + 1);
                userDao.update(user);
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

    @Override
    public SuccessAuthResponse validateUserDevice(UsernameAndPasswordAuthenticationRequestDto userDto) throws DeviceAuthException, AppSignAuthException {
        SuccessAuthResponse successAuthResponse = new SuccessAuthResponse();
        UserType userType = new UserType();
        userType.setId(DeviceTypeEnum.MPOS.getValue());

        User user = userDao.findByUserNameAndUserTypeAndDevice_SerialNo(userDto.getUsername(), userType,
                userDto.getDeviceSerial());

        /*
         need to check whether the attempt count has exceeded, if true account should be blocked,
         there should be an option to reset such accounts
         */
        if (user.getLoginAttempt() > maximumInvalidAttemptCount) {
//            extracted(userDto.getUsername());
            user.setStatus(new Status("DACT"));
            try {
                userDao.update(user);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
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

        ApplicationSignature applicationSignature = appSignatureDao.findByAppVersion(userDto.getAppVersion());

        if (!applicationSignature.getAppSignature().equals(userDto.getSignature())) {
            throw new AppSignAuthException("Tamper detected");
        }
        if (applicationSignature.getSysfileSize() != userDto.getSysFileSize()) {
            throw new AppSignAuthException("Tamper detected");
        }

        DeviceConfig deviceConfig = deviceConfigDao.findByDevice_Id(user.getDevice().getId());

        if (!StatusVarList.ACTIVE_STATUS_CODE.equals(deviceConfig.getStatus().getStatusCode())) {
            throw new DeviceAuthException("No active profile configuration found for this device");
        }

        DeviceConfigResponseDto responseDto = null;
        try {
            responseDto = objectMapper.readValue(deviceConfig.getConfig(), DeviceConfigResponseDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        successAuthResponse.setProfileData(responseDto);

        if (user.getFirstLogin() == 0) {
            successAuthResponse.setInitialLogin(true);
            successAuthResponse.setOtpRequired(true);
            successAuthResponse.setOtpLifeTime(otpExpireAfterMinutes);
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
                if (user.getStatus().getStatusCode().equals("ACTV")) {
                    user.setLoginAttempt(user.getLoginAttempt() - 1);
                    try {
                        userDao.update(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                throw new DeviceAuthException("There is an issue in sending emails");
            }
            otpDao.create(onetimePassword);
            /*
            This is the first time login to the device, should send the email,OTP validation
            there should be an option to send and verify OTP
            OTP can be save either in the redis cache or in the Database by hashing
             */

        } else {
            /*
             need to update last login time, login attempt count reset
             */
            successAuthResponse.setInitialLogin(false);
            user.setLoginAttempt(0);
            user.setLastLoginTime(new Date());
            try {
                userDao.update(user);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        return successAuthResponse;
    }

    @Override
    @Transactional
    public boolean validateOTP(String otp) throws Exception {
        UserType userType = new UserType();
        userType.setId(DeviceTypeEnum.MPOS.getValue());

        User user = dao.loadUserByUsername(userBeanUtil.getUsername(), userType);

        OnetimePassword onetimePassword = otpDao.findByUser(user);
        String storedOtp = onetimePassword.getValue();

        if (passwordEncoder.matches(otp, storedOtp) && onetimePassword.getExpireson().compareTo(new Date()) > 0) {
            user.setFirstLogin(1);
            userDao.update(user);
            otpDao.deleteById(onetimePassword.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public String changePassword(ChangePasswordRequestDto requestDto) throws Exception {

        UserType userType = new UserType();
        userType.setId(DeviceTypeEnum.MPOS.getValue());

        User entity = dao.loadUserByUsername(userBeanUtil.getUsername(), userType);

        if (passwordEncoder.matches(requestDto.getCurrentPassword(), entity.getPassword())) {
            entity.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
            userDao.update(entity);
        } else {
            throw new NotFoundException("Fail - Old Password mismatch");
        }

        return "success";
    }

    private SimpleGrantedAuthority convertToSimpleGrant(UserRole userrole) {
        return new SimpleGrantedAuthority("ROLE_" + userrole.getRole().getRoleName());
    }
}
