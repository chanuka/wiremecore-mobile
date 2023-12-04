package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.dto.ApplicationUserDto;
import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
import com.cba.core.wirememobile.exception.AppSignAuthException;
import com.cba.core.wirememobile.exception.DeviceAuthException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsDao {

    ApplicationUserDto loadUserByUsername(String username) throws UsernameNotFoundException;

    boolean validateUserDevice(UsernameAndPasswordAuthenticationRequestDto userDto) throws DeviceAuthException, AppSignAuthException;
}
