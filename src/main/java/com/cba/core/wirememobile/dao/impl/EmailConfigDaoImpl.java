package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.EmailConfigDao;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.EmailConfig;
import com.cba.core.wirememobile.repository.EmailConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailConfigDaoImpl implements EmailConfigDao {

    private final EmailConfigRepository repository;

    @Override
    public EmailConfig findByAction(String action) throws Exception {
        return repository.findByAction(action)
                .orElseThrow(() -> new NotFoundException("Email config Not Found for given action"));
    }
}
