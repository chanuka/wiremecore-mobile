package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.model.TokenBlacklist;

import java.sql.SQLException;

public interface TokenBlacklistService {

     TokenBlacklist createBlacklistToken(String token) throws Exception;

     boolean isTokenBlacklisted(String token) throws Exception;
}
