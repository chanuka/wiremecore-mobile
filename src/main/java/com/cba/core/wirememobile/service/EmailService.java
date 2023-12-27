package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.EReceiptDataDto;
import com.cba.core.wirememobile.dto.EmailRequestDto;

public interface EmailService {

    void sendEmail(EmailRequestDto emailRequestDto) throws Exception;

    void sendEmail(String userMail, String password) throws Exception;

    void sendEmail(String userMail, EReceiptDataDto data) throws Exception;

}
