package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.EReceiptDao;
import com.cba.core.wirememobile.model.EReceipt;
import com.cba.core.wirememobile.repository.EReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EReceiptDaoImpl implements EReceiptDao {

    private final EReceiptRepository repository;

    @Override
    public EReceipt create(EReceipt eReceipt) throws Exception {
        return repository.save(eReceipt);
    }
}
