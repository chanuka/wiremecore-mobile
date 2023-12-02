package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.TokenRefresh;

import java.io.IOException;
import java.util.Optional;

public interface RefreshTokenDao {

    public Optional<TokenRefresh> findByToken(String token) throws Exception;

    public TokenRefresh createRefreshToken(String userName) throws IOException;

    public int deleteByUserId(Integer userId) throws Exception;

    public TokenRefresh verifyExpiration(TokenRefresh token);
}
