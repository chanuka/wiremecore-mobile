package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.EmailConfig;

public interface EmailConfigDao {

    EmailConfig findByAction(String action) throws Exception;
}
