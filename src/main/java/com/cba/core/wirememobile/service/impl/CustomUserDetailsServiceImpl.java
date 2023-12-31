package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.CustomUserDetailsDao;
import com.cba.core.wirememobile.dto.ApplicationUserDto;
import com.cba.core.wirememobile.dto.ChangePasswordRequestDto;
import com.cba.core.wirememobile.dto.SuccessAuthResponse;
import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
import com.cba.core.wirememobile.exception.AppSignAuthException;
import com.cba.core.wirememobile.exception.DeviceAuthException;
import com.cba.core.wirememobile.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final CustomUserDetailsDao customUserDetailsDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            ApplicationUserDto applicationUser = customUserDetailsDao.loadUserByUsername(username);
            return applicationUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    @Override
    public SuccessAuthResponse validateUserDevice(UsernameAndPasswordAuthenticationRequestDto userDto) throws DeviceAuthException, AppSignAuthException {
        return customUserDetailsDao.validateUserDevice(userDto);
    }

    @Override
    public boolean validateOTP(String otp) throws Exception {
        return customUserDetailsDao.validateOTP(otp);
    }

    @Override
    public String changePassword(ChangePasswordRequestDto requestDto) throws Exception {
        return customUserDetailsDao.changePassword(requestDto);
    }
}
