package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.DeviceDao;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.Device;
import com.cba.core.wirememobile.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeviceDaoImpl implements DeviceDao {

    private final DeviceRepository repository;

    @Override
    public Device create(Device device) throws Exception {
        return repository.save(device);
    }

    @Override
    public Device findByTransactionTerminal(String terminalId, String serialNo) throws Exception {
        return repository.findByTransactionTerminal(terminalId, serialNo).orElseThrow(() -> new NotFoundException("Device Not Found"));
    }

    @Override
    public Device findBySerialNo(String serialNo) throws Exception {
        return repository.findBySerialNo(serialNo).orElseThrow(() -> new NotFoundException("Device Not Found"));
    }
}
