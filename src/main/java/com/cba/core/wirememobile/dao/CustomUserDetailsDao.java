package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.User;
import com.cba.core.wirememobile.model.UserType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsDao {

    User loadUserByUsername(String username, UserType userType) throws UsernameNotFoundException;

}
