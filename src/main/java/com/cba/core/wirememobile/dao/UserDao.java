package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.User;
import com.cba.core.wirememobile.model.UserType;

public interface UserDao {

    User update(User user) throws Exception;

    User findByUserNameAndUserTypeAndDevice_SerialNo(String userName, UserType userType,String serialNo);

    User findByUserName(String userName);

}
