package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.Device;

public interface DeviceDao {

    Device create(Device device) throws Exception;

    Device findByTransactionTerminal(String terminalId, String serialNo) throws Exception;

    Device findBySerialNo(String serialNo) throws Exception;
}
