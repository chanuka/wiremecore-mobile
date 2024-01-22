package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.TokenBlacklistDao;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.TokenBlacklist;
import com.cba.core.wirememobile.repository.TokenBlacklistRepository;
import com.cba.core.wirememobile.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenBlacklistDaoImpl implements TokenBlacklistDao {

    private final TokenBlacklistRepository repository;

    @Override
    public TokenBlacklist createBlacklistToken(TokenBlacklist tokenBlacklist) throws Exception {
        return repository.save(tokenBlacklist);

    }

//    @Override
//    public boolean isTokenBlacklisted(String token) throws SQLException {
//        Optional<TokenBlacklist> tokenBlacklist = repository.findByToken(token);
//        return tokenBlacklist != null
//                && !tokenBlacklist.isEmpty()
//                && tokenBlacklist.get().getExpiration().isAfter(Instant.now());
//    }

    @Override
    public Optional<TokenBlacklist> findByToken(String token) throws Exception {
        return repository.findByToken(token);
    }

}
