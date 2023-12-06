package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.OnetimePassword;
import com.cba.core.wirememobile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OnetimePassword, Integer>, JpaSpecificationExecutor<OnetimePassword> {

    Optional<OnetimePassword> findByUser(User user);
}
