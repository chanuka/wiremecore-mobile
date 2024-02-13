package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.EReceiptDataDto;

public interface EmailService {

    void sendEmail(String userMail, String password) throws Exception;

    void sendEmail(int id,EReceiptDataDto data) throws Exception;

}
