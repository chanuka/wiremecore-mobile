package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.model.TokenRefresh;

import java.io.IOException;
import java.util.Optional;

public interface RefreshTokenService {

    Optional<TokenRefresh> findByToken(String token) throws Exception;

    TokenRefresh createRefreshToken(String userName) throws IOException;

    TokenRefresh verifyExpiration(TokenRefresh token); // should be fixed

    int deleteByUserName(String userName) throws Exception;
}
