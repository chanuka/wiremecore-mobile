package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.TransactionDao;
import com.cba.core.wirememobile.dto.TransactionCoreResponseDto;
import com.cba.core.wirememobile.dto.TransactionRequestDto;
import com.cba.core.wirememobile.dto.TransactionResponseDto;
import com.cba.core.wirememobile.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;


    @Override
    public TransactionResponseDto create(TransactionRequestDto requestDto) throws Exception {
        return transactionDao.create(requestDto);
    }

    @Override
    public TransactionResponseDto createFailed(TransactionRequestDto requestDto) throws Exception {
        return transactionDao.createFailed(requestDto);
    }

    @Override
    public Page<TransactionCoreResponseDto> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        return transactionDao.getAllTransactions(dateFrom, dateTo, page, pageSize);
    }

    @Override
    public Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception {
        return transactionDao.getAllTransactionSummary(dateFrom, dateTo, queryBy);
    }
}
