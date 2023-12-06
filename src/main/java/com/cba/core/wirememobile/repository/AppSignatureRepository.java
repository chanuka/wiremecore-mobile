package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.ApplicationSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppSignatureRepository extends JpaRepository<ApplicationSignature, Integer>, JpaSpecificationExecutor<ApplicationSignature> {

    Optional<ApplicationSignature> findByAppVersion(String versionNo);

}
