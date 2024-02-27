package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.EReceiptDataDto;
import com.cba.core.wirememobile.dto.SettlementDetailEmailDto;
import com.cba.core.wirememobile.dto.SettlementEmailDto;

public interface EmailService {

    void sendEmail(String userMail, String password) throws Exception;

    void sendEmail(int id,EReceiptDataDto data) throws Exception;

    void sendEmail(SettlementEmailDto settlementEmailDto) throws RuntimeException;

    void sendEmail(SettlementDetailEmailDto settlementDetailEmailDto) throws RuntimeException;


}
