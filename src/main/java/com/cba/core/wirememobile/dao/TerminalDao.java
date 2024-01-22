package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.Terminal;

public interface TerminalDao {

    Terminal findByTerminalId(String terminalId) throws Exception;
}
