package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.DeviceConfig;
import com.cba.core.wirememobile.model.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmailConfigRepository extends JpaRepository<EmailConfig, Integer>, JpaSpecificationExecutor<EmailConfig> {

    Optional<EmailConfig> findByAction(String action);

}
