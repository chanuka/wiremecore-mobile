package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.dto.ApplicationUserDto;
import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

public interface CustomUserDetailsDao {

    ApplicationUserDto loadUserByUsername(String username) throws UsernameNotFoundException;

    boolean validateUserDevice(UsernameAndPasswordAuthenticationRequestDto userDto) throws IOException;
}
