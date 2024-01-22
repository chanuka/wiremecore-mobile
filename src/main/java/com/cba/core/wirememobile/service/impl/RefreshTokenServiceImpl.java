package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.config.RefreshTokenConfig;
import com.cba.core.wirememobile.dao.RefreshTokenDao;
import com.cba.core.wirememobile.dao.UserDao;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.exception.TokenRefreshException;
import com.cba.core.wirememobile.model.TokenRefresh;
import com.cba.core.wirememobile.model.User;
import com.cba.core.wirememobile.repository.UserRepository;
import com.cba.core.wirememobile.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenDao dao;
    private final UserDao userDao;
    private final RefreshTokenConfig refreshTokenConfig;


    @Override
    public Optional<TokenRefresh> findByToken(String token) throws Exception {
        return dao.findByToken(token);
    }

    @Override
    public TokenRefresh createRefreshToken(String userName) throws IOException {

        TokenRefresh refreshToken = new TokenRefresh();
        User user = userDao.findByUserName(userName);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenConfig.getTokenExpirationAfterMillis()));
        refreshToken.setToken(UUID.randomUUID().toString());

        return dao.createRefreshToken(refreshToken);

    }

    @Override
    public TokenRefresh verifyExpiration(TokenRefresh token) {// need to add exception handling

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            try {
                dao.deleteByToken(token); // this has to be fixed
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new sign-in request");
        }
        return token;
    }

    @Override
    public int deleteByUserName(String userName) throws Exception {
        return dao.deleteByUser(userDao.findByUserName(userName));
    }
}
