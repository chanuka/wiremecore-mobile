package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.ApplicationSignature;

public interface AppSignatureDao {

    ApplicationSignature findByAppVersion(String appVersion);
}
