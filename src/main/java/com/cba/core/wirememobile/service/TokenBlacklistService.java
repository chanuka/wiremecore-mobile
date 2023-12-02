package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.model.TokenBlacklist;

import java.sql.SQLException;

public interface TokenBlacklistService {

    public TokenBlacklist createBlacklistToken(String token) throws Exception;

    public boolean isTokenBlacklisted(String token) throws SQLException;
}
