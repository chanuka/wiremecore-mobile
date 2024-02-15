package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dto.EReceiptDataDto;
import com.cba.core.wirememobile.dto.EmailRequestDto;
import com.cba.core.wirememobile.dto.SettlementEmailDto;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.EReceipt;
import com.cba.core.wirememobile.repository.EReceiptRepository;
import com.cba.core.wirememobile.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {


    @Value("${application.email.url}")
    private String emailUrl;

    private final RestTemplate restTemplate;
    private final EReceiptRepository eReceiptRepository;

//    @Qualifier("asyncExecutor")
//    private final TaskExecutor taskExecutor;


    @Override
    @Async("asyncExecutor")
    public void sendEmail(String userMail, String messageBody) throws Exception {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setTo(userMail);
            emailRequestDto.setBody(messageBody);
            emailRequestDto.setSubject("This is auto generated email - include credentials/OTP");
            emailRequestDto.setIsHtml(false);

            HttpEntity<EmailRequestDto> requestEntity = new HttpEntity<>(emailRequestDto, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(emailUrl + "/email", requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseData = response.getBody();
                // Process responseData here
                System.out.println("Response: " + responseData);
            } else {
                System.out.println("Error occurred. Status code: " + response.getStatusCodeValue());
                // Handle other error cases
            }
        } catch (Exception ex) {
            System.out.println("Error occurred: " + ex.getMessage());
            // Handle exceptions
        }
    }

    @Override
    @Async("asyncExecutor")
    public void sendEmail(int id, EReceiptDataDto data) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EReceiptDataDto> requestEntity = new HttpEntity<>(data, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(emailUrl + "/receipt-email", requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseData = response.getBody();
                // Process responseData here
                System.out.println("Response: " + responseData);

                EReceipt eReceipt = eReceiptRepository.findById(id).orElseThrow(() -> new NotFoundException("e-Receipt Info Not Found"));
                eReceipt.setIs_sent_mail(true);
                eReceiptRepository.save(eReceipt);
            } else {
                System.out.println("Error occurred. Status code: " + response.getStatusCode().value());
                // Handle other error cases
            }
        } catch (Exception ex) {
            System.out.println("Error occurred: " + ex.getMessage());
            // Handle exceptions
        }


    }

    @Override
    @Async("asyncExecutor")
    public void sendEmail(SettlementEmailDto settlementEmailDto) throws RuntimeException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SettlementEmailDto> requestEntity = new HttpEntity<>(settlementEmailDto, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(emailUrl + "/settlement-email", requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseData = response.getBody();
                // Process responseData here
                System.out.println("Response: " + responseData);

//                EReceipt eReceipt = eReceiptRepository.findById(id).orElseThrow(() -> new NotFoundException("e-Receipt Info Not Found"));
//                eReceipt.setIs_sent_mail(true);
//                eReceiptRepository.save(eReceipt);
            } else {
                System.out.println("Error occurred. Status code: " + response.getStatusCode().value());
                // Handle other error cases
            }
        } catch (Exception ex) {
            System.out.println("Error occurred: " + ex.getMessage());
            // Handle exceptions
        }
    }
}
