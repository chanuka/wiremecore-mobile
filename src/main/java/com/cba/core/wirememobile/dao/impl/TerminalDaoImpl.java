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
    public Terminal findByTerminalId(String terminalId) throws RuntimeException {
        return repository.findByTerminalId(terminalId).orElseThrow(() -> new NotFoundException("Terminal Not Found"));
    }

    @Override
    public Terminal findByTerminalIdAndMerchant_MerchantIdAndDevice_SerialNo(String terminalId, String merchantId,String serialNo) throws RuntimeException {
        return repository.findByTerminalIdAndMerchant_MerchantIdAndDevice_SerialNo(terminalId, merchantId,serialNo).orElseThrow(() -> new NotFoundException("Terminal Not Found"));
    }
}
