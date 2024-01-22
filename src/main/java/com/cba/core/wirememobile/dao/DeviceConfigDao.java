package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.DeviceConfig;

public interface DeviceConfigDao {

    DeviceConfig findByDevice_Id(int deviceId);
}
