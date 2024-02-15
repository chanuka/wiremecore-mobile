package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.Terminal;

public interface TerminalDao {

    Terminal findByTerminalId(String terminalId) throws RuntimeException;

    Terminal findByTerminalIdAndMerchant_MerchantIdAndDevice_SerialNo(String terminalId, String merchantId,String serialNo) throws RuntimeException;
}
