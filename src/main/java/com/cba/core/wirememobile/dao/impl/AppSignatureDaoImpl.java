package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.AppSignatureDao;
import com.cba.core.wirememobile.exception.AppSignAuthException;
import com.cba.core.wirememobile.model.ApplicationSignature;
import com.cba.core.wirememobile.repository.AppSignatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AppSignatureDaoImpl implements AppSignatureDao {

    private final AppSignatureRepository repository;

    @Override
    public ApplicationSignature findByAppVersion(String appVersion) {
        return repository.findByAppVersion(appVersion)
                .orElseThrow(() -> new AppSignAuthException("Application Signature not found"));
    }
}
