package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dto.EReceiptDataDto;
import com.cba.core.wirememobile.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    //    @Qualifier("asyncExecutor")
//    private final TaskExecutor taskExecutor;

    @Override
    @Async("asyncExecutor")
    public void sendSms(String to, String message) throws Exception {
        System.out.println("SMS has been sent successfully");
    }

    @Override
    @Async("asyncExecutor")
    public void sendSms(String to, EReceiptDataDto receiptDataDto) throws Exception {
        Thread.sleep(5000);
        System.out.println("Merchant Copy SMS has been sent successfully..!!!");
    }
}
