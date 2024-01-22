package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.TerminalDao;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.Terminal;
import com.cba.core.wirememobile.repository.TerminalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TerminalDaoImpl implements TerminalDao {

    private final TerminalRepository repository;

    @Override
    public Terminal findByTerminalId(String terminalId) throws Exception {
        return repository.findByTerminalId(terminalId).orElseThrow(() -> new NotFoundException("Terminal Not Found"));
    }
}
