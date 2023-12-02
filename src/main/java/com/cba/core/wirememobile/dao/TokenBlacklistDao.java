package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.TokenBlacklist;

import java.sql.SQLException;

public interface TokenBlacklistDao {

    public TokenBlacklist createBlacklistToken(String token) throws Exception;

    public boolean isTokenBlacklisted(String token) throws SQLException;

}
