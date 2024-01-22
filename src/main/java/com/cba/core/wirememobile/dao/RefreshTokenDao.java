package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.TokenRefresh;
import com.cba.core.wirememobile.model.User;

import java.io.IOException;
import java.util.Optional;

public interface RefreshTokenDao {

    Optional<TokenRefresh> findByToken(String token) throws Exception;

    TokenRefresh createRefreshToken(TokenRefresh token) throws IOException;

    int deleteByUser(User user) throws Exception;

    void deleteByToken(TokenRefresh token) throws Exception;

}
