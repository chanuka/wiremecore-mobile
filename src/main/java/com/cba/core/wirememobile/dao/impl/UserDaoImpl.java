package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.UserDao;
import com.cba.core.wirememobile.exception.DeviceAuthException;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.User;
import com.cba.core.wirememobile.model.UserType;
import com.cba.core.wirememobile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;

    @Override
    public User update(User user) throws Exception {
        return userRepository.save(user);
    }

    @Override
    public User findByUserNameAndUserTypeAndDevice_SerialNo(String userName, UserType userType, String serialNo) {
        return userRepository.findByUserNameAndUserTypeAndDevice_SerialNo(userName, userType,
                serialNo)
                .orElseThrow(() -> new DeviceAuthException("No device found with given serial"));
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new NotFoundException("User Not Found"));
    }
}
