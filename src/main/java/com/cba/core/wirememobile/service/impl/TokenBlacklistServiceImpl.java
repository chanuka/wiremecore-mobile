package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.TokenBlacklistDao;
import com.cba.core.wirememobile.model.TokenBlacklist;
import com.cba.core.wirememobile.service.TokenBlacklistService;
import com.cba.core.wirememobile.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final TokenBlacklistDao dao;
    private final JwtUtil jwtUtil;
    private final JwtDecoder decoder;

    @Override
    public TokenBlacklist createBlacklistToken(String token) throws Exception {

        TokenBlacklist tokenBlacklist = null;
        if (isTokenBlacklisted(token)) {
            return tokenBlacklist;
        }
        tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);
        tokenBlacklist.setExpiration(extractTokenExpiration(token));

        return dao.createBlacklistToken(tokenBlacklist);

    }

    @Override
    public boolean isTokenBlacklisted(String token) throws Exception {

        Optional<TokenBlacklist> tokenBlacklist = dao.findByToken(token);
        return tokenBlacklist != null
                && !tokenBlacklist.isEmpty()
                && tokenBlacklist.get().getExpiration().isAfter(Instant.now());
    }

    private Instant extractTokenExpiration(String token) throws Exception {
        Jwt claimsJws = jwtUtil.validateJwtToken(token, decoder);
        return claimsJws.getExpiresAt();
    }
}
