package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist,Integer> {

    Optional<TokenBlacklist> findByToken(String token);
}
