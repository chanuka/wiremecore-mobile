package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.EReceiptDataDto;

public interface SmsService {

    void sendSms(String to, String message)throws Exception;

    void sendSms(String to, EReceiptDataDto receiptDataDto)throws Exception;
}
