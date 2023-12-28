package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dto.EReceiptDataDto;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.EReceipt;
import com.cba.core.wirememobile.repository.EReceiptRepository;
import com.cba.core.wirememobile.service.SmsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SmsServiceImpl implements SmsService {

    private final EReceiptRepository eReceiptRepository;

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
        EReceipt eReceipt = eReceiptRepository.findById(receiptDataDto.getId()).orElseThrow(() -> new NotFoundException("e-Receipt Info Not Found"));
        eReceipt.setIsSentSms(true);
        eReceiptRepository.save(eReceipt);
        System.out.println("SMS has been sent successfully..!!!");
    }
}
