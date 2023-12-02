package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.service.SmsService;

public class SmsServiceImpl implements SmsService {

    @Override
    public void sendSms(String to, String message) {
        System.out.println("SMS has been sent successfully");
    }
}
