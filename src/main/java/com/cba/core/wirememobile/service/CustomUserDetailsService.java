package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.ChangePasswordRequestDto;
import com.cba.core.wirememobile.dto.SuccessAuthResponse;
import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
import com.cba.core.wirememobile.exception.AppSignAuthException;
import com.cba.core.wirememobile.exception.DeviceAuthException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    SuccessAuthResponse validateUserDevice(UsernameAndPasswordAuthenticationRequestDto userDto) throws DeviceAuthException, AppSignAuthException;

    boolean validateOTP(String otp) throws Exception;

    String changePassword(ChangePasswordRequestDto requestDto) throws Exception;

}
