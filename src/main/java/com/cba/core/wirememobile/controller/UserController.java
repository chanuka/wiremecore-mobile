package com.cba.core.wirememobile.controller;

import com.cba.core.wirememobile.controller.resource.UserResource;
import com.cba.core.wirememobile.dto.ChangePasswordRequestDto;
import com.cba.core.wirememobile.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@RequestMapping("/${application.resource.users}")
@Tag(name = "User Management", description = "Provides User Management API's")
public class UserController implements UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final MessageSource messageSource;
    private final CustomUserDetailsService service;
    private final ObjectMapper objectMapper;


    @Override
    public ResponseEntity<String> changePassword(ChangePasswordRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_CHANGE_PASSWORD_DEBUG", null, currentLocale));

        try {
            service.changePassword(requestDto);
            return ResponseEntity.ok().body(messageSource.getMessage("USER_CHANGE_PASSWORD_SUCCESS", null, currentLocale));

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> ValidateOTP(String otp) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_OTP_VERIFY_DEBUG", null, currentLocale));

        try {
            JsonNode jsonNode = objectMapper.readTree(otp);
            String propertyValue = jsonNode.get("otp").asText();

            if (service.validateOTP(propertyValue)) {
                return ResponseEntity.ok().body(messageSource.getMessage("USER_OTP_VERIFY_SUCCESS", null, currentLocale));
            } else {
                return ResponseEntity.ok().body(messageSource.getMessage("USER_OTP_VERIFY_FAIL", null, currentLocale));
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
