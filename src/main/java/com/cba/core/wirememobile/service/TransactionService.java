package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.TransactionCoreResponseDto;
import com.cba.core.wirememobile.dto.TransactionRequestDto;
import com.cba.core.wirememobile.dto.TransactionResponseDto;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Map;

public interface TransactionService {

    TransactionResponseDto create(TransactionRequestDto requestDto) throws Exception;

    TransactionResponseDto createFailed(TransactionRequestDto requestDto) throws Exception;

    Page<TransactionCoreResponseDto> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception;

    Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception;

}
