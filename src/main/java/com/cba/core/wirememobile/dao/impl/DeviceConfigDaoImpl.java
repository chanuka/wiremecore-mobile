package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.DeviceConfigDao;
import com.cba.core.wirememobile.exception.DeviceAuthException;
import com.cba.core.wirememobile.model.DeviceConfig;
import com.cba.core.wirememobile.repository.DeviceConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeviceConfigDaoImpl implements DeviceConfigDao {

    private final DeviceConfigRepository repository;

    @Override
    public DeviceConfig findByDevice_Id(int deviceId) {
        return repository.findByDevice_Id(deviceId).
                orElseThrow(() -> new DeviceAuthException("No profile configuration found for this device"));
    }
}
