package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.GeneralDao;
import com.cba.core.wirememobile.model.CrashTrace;
import com.cba.core.wirememobile.repository.CrashTraceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GeneralDaoImpl implements GeneralDao {

    private final CrashTraceRepository repository;

    @Override
    public CrashTrace create(CrashTrace trace) throws Exception {
        return repository.save(trace);
    }
}
