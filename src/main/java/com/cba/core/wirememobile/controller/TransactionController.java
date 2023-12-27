package com.cba.core.wirememobile.controller;

import com.cba.core.wirememobile.controller.resource.TransactionResource;
import com.cba.core.wirememobile.dto.*;
import com.cba.core.wirememobile.service.TransactionService;
import com.cba.core.wirememobile.util.PaginationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
@RequestMapping("/${application.resource.transactions}")
@Tag(name = "Transaction Management", description = "Provides Transaction Management API's")
public class TransactionController implements TransactionResource {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<String> generateEReceipt(EReceiptRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TRANSACTION_POST_E-RECEIPT_DEBUG", null, currentLocale));
        try {
            String response = transactionService.generateEReceipt(requestDto);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> generateEReceipt(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TRANSACTION_POST_E-RECEIPT_DEBUG", null, currentLocale));
        try {
            String response = transactionService.generateEReceipt(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }    }

    @Override
    public ResponseEntity<Map<String, ArrayList<Map<String, Object>>>> getAllDeviceTransactionSummary(String dateFrom,
                                                                                                      String dateTo,
                                                                                                      String queryBy,
                                                                                                      String originId,
                                                                                                      String deviceSerial) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TRANSACTION_GET_SUMMARY_DEBUG", null, currentLocale));
        try {
            Map<String, ArrayList<Map<String, Object>>> responseDtolist = transactionService.getAllTransactionSummary(dateFrom, dateTo, queryBy);
            return ResponseEntity.ok().body(responseDtolist);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<PaginationResponse<TransactionCoreResponseDto>> getAllDeviceTransactions(String dateFrom,
                                                                                                   String dateTo,
                                                                                                   String originId,
                                                                                                   String deviceSerial,
                                                                                                   int isSettled,
                                                                                                   int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TRANSACTION_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<TransactionCoreResponseDto> responseDtolist = transactionService.getAllTransactions(dateFrom, dateTo, page, pageSize);
            return ResponseEntity.ok().body(new PaginationResponse<TransactionCoreResponseDto>(responseDtolist.getContent(), responseDtolist.getTotalElements()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> settleTransaction(SettlementRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TRANSACTION_SETTLE_ONE_DEBUG", null, currentLocale));
        try {
            transactionService.settlement(requestDto);
            return ResponseEntity.ok().body("Settlement Success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<TransactionResponseDto> createFailedTransaction(TransactionRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("TRANSACTION_FAILED_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            TransactionResponseDto responseDto = transactionService.createFailed(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<TransactionResponseDto> createTransaction(TransactionRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("TRANSACTION_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            TransactionResponseDto responseDto = transactionService.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }


    @Override
    public ResponseEntity<PaginationResponse<TransactionCoreResponseDto>> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TRANSACTION_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<TransactionCoreResponseDto> responseDtolist = transactionService.getAllTransactions(dateFrom, dateTo, page, pageSize);
            return ResponseEntity.ok().body(new PaginationResponse<TransactionCoreResponseDto>(responseDtolist.getContent(), responseDtolist.getTotalElements()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<Map<String, ArrayList<Map<String, Object>>>> getAllTransactionSummary(String dateFrom, String dateTo,
                                                                                                String queryBy) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TRANSACTION_GET_SUMMARY_DEBUG", null, currentLocale));
        try {
            Map<String, ArrayList<Map<String, Object>>> responseDtolist = transactionService.getAllTransactionSummary(dateFrom, dateTo, queryBy);
            return ResponseEntity.ok().body(responseDtolist);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
