package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.dto.*;
import com.cba.core.wirememobile.model.EReceipt;
import com.cba.core.wirememobile.model.TransactionCore;
import com.cba.core.wirememobile.model.TransactionCoreFailed;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TransactionDao {

    TransactionCore findByRrnAndInvoiceNoAndTraceNo(String serialNo, String rrn, int invoiceNo, int traceNo) throws Exception;

    TransactionCore findById(int id) throws Exception;

    TransactionCore create(TransactionCore toInsert) throws Exception;

    TransactionCoreFailed createFailed(TransactionCoreFailed toInsert) throws Exception;

    int updateRecordsWithCondition(boolean isSettled,int settledMethod,String originId,String merchantId,
                                   String terminalId,int batchNo) throws Exception;

    Page<TransactionCore> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception;

    List<Object[]> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy,
                                            String whereClause,
                                            String selectClause,
                                            String groupByClause) throws Exception;

}
