package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.OnetimePassword;
import com.cba.core.wirememobile.model.User;

public interface OtpDao {

    OnetimePassword findByUser(User user);

    void deleteById(int id);

    OnetimePassword create(OnetimePassword onetimePassword);

}
