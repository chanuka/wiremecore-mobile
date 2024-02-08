package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.TokenRefresh;
import com.cba.core.wirememobile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<TokenRefresh, Long> {
    Optional<TokenRefresh> findByToken(String token);

    @Modifying
    int deleteByUser(User user);
}
