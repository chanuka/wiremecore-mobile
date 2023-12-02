package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

public interface CustomUserDetailsService extends UserDetailsService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    boolean validateUserDevice(UsernameAndPasswordAuthenticationRequestDto userDto) throws IOException;

}
