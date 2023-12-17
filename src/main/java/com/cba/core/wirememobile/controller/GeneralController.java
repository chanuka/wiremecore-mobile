package com.cba.core.wirememobile.controller;

import com.cba.core.wirememobile.controller.resource.GeneralResource;
import com.cba.core.wirememobile.dto.CrashTraceRequestDto;
import com.cba.core.wirememobile.dto.CrashTraceResponseDto;
import com.cba.core.wirememobile.dto.TransactionResponseDto;
import com.cba.core.wirememobile.service.GeneralService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Tag(name = "General API Management", description = "This is included all general purpose API list")
public class GeneralController implements GeneralResource {

    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);
    private final MessageSource messageSource;
    private final GeneralService service;

    @Override
    public ResponseEntity<CrashTraceResponseDto> setCrashReport(CrashTraceRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("CRASH_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            CrashTraceResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
