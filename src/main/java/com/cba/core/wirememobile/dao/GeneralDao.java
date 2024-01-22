package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.CrashTrace;

public interface GeneralDao {

    CrashTrace create(CrashTrace trace) throws Exception;
}
