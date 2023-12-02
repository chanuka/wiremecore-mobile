package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.TokenBlacklistDao;
import com.cba.core.wirememobile.model.TokenBlacklist;
import com.cba.core.wirememobile.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final TokenBlacklistDao tokenBlacklistDao;

    @Override
    public TokenBlacklist createBlacklistToken(String token) throws Exception {
        TokenBlacklist tokenBlacklist = tokenBlacklistDao.createBlacklistToken(token);
        return tokenBlacklist;
    }

    @Override
    public boolean isTokenBlacklisted(String token) throws SQLException {
        boolean isTokenBlackListed = tokenBlacklistDao.isTokenBlacklisted(token);
        return isTokenBlackListed;
    }
}
