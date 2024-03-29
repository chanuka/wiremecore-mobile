package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.RefreshTokenDao;
import com.cba.core.wirememobile.model.TokenRefresh;
import com.cba.core.wirememobile.model.User;
import com.cba.core.wirememobile.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenDaoImpl implements RefreshTokenDao {

    private final RefreshTokenRepository repository;

    @Override
    public Optional<TokenRefresh> findByToken(String token) throws Exception {
        return repository.findByToken(token);
    }

    @Override
    public TokenRefresh createRefreshToken(TokenRefresh token) throws IOException {
        return repository.save(token);
    }

    @Override
    public int deleteByUser(User user) throws Exception {
        return repository.deleteByUser(user);
    }

    @Override
    public void deleteByToken(TokenRefresh token) throws Exception {
        repository.delete(token);
    }

}
