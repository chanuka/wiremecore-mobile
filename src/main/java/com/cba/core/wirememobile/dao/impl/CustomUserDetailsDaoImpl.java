package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.CustomUserDetailsDao;
import com.cba.core.wirememobile.model.User;
import com.cba.core.wirememobile.model.UserType;
import com.cba.core.wirememobile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserDetailsDaoImpl implements CustomUserDetailsDao {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String userName,UserType userType) throws UsernameNotFoundException {
            return userRepository.findByUserNameAndUserType(userName, userType).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
