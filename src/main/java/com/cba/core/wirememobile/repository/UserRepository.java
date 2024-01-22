package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.User;
import com.cba.core.wirememobile.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserNameAndUserType(String userName, UserType userType);

    Optional<User> findByUserNameAndUserTypeAndDevice_SerialNo(String userName, UserType userType, String serialNo);

}
