package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.TransactionCore;
import com.cba.core.wirememobile.model.TransactionCoreFailed;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface TransactionDao {

    TransactionCore findByRrnAndInvoiceNoAndTraceNo(String serialNo, String rrn, int invoiceNo, int traceNo, Date dateTime) throws Exception;

    TransactionCore findById(int id) throws Exception;

    TransactionCore create(TransactionCore toInsert) throws Exception;

    TransactionCoreFailed createFailed(TransactionCoreFailed toInsert) throws Exception;

    List<TransactionCore> getAllTransactionsByMerchantIdAndTerminalIdAndBatchNoAndOriginId(String merchantId,
                                                                                           String terminalId,
                                                                                           int batchNo,
                                                                                           String originId) throws RuntimeException;

    int updateRecordsWithCondition(boolean isSettled, int settledMethod, String originId, String merchantId,
                                   String terminalId, int batchNo) throws RuntimeException;

    Page<TransactionCore> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception;

    List<Object[]> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy,
                                            String whereClause,
                                            String selectClause,
                                            String groupByClause) throws Exception;

}
