package com.cba.core.wirememobile.controller;

import com.cba.core.wirememobile.dto.EmailRequestDto;
import com.cba.core.wirememobile.service.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Email Management(Optional)", description = "Provides Email Sending API's")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/devices/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDto emailRequestDto) throws Exception {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("name", "Chanuka !");
            model.put("location", "Sri Lanka");
            model.put("sign", "Java Developer");
            model.put("type", "NEWSLETTER");
            emailRequestDto.setProps(model);

            emailService.sendEmail(emailRequestDto);
            return ResponseEntity.ok("Email sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}