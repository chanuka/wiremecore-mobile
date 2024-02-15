package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.MerchantDao;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.Merchant;
import com.cba.core.wirememobile.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class MerchantDaoImpl implements MerchantDao {

    private final MerchantRepository repository;

    @Override
    public Merchant findByMerchantId(String merchantId) throws RuntimeException {
        return repository.findByMerchantId(merchantId).orElseThrow(() -> new NotFoundException("Merchant Not Found"));
    }
}
