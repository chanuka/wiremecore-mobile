package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.dto.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Map;

public interface TransactionDao {

    void settlement(SettlementRequestDto requestDto) throws Exception;

    TransactionResponseDto create(TransactionRequestDto requestDto) throws Exception;

    TransactionResponseDto createFailed(TransactionRequestDto requestDto) throws Exception;

    SettlementResponseDto updateSettlement(SettlementRequestDto requestDto) throws Exception;

    Page<TransactionCoreResponseDto> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception;

    Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception;

}
