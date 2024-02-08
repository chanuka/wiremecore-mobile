package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.TokenBlacklist;

import java.util.Optional;

public interface TokenBlacklistDao {

    TokenBlacklist createBlacklistToken(TokenBlacklist tokenBlacklist) throws Exception;

//    boolean isTokenBlacklisted(String token) throws SQLException;

    Optional<TokenBlacklist> findByToken(String token) throws Exception;

}
