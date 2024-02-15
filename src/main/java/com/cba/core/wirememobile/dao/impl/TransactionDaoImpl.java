package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.TransactionDao;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.TransactionCore;
import com.cba.core.wirememobile.model.TransactionCoreFailed;
import com.cba.core.wirememobile.repository.TransactionFailedRepository;
import com.cba.core.wirememobile.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

    private final TransactionRepository transactionRepository;
    private final TransactionFailedRepository transactionFailedRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TransactionCore findByRrnAndInvoiceNoAndTraceNo(String serialNo, String rrn, int invoiceNo, int traceNo, Date dateTime) throws Exception {
        return transactionRepository.findByRrnAndInvoiceNoAndTraceNo(
                serialNo,
                rrn,
                invoiceNo,
                traceNo,
                dateTime
        ).orElseThrow(() -> new NotFoundException("Transaction Not Found"));
    }

    @Override
    public TransactionCore findById(int id) throws Exception {
        return transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction Not Found"));
    }


    @Override
    public TransactionCore create(TransactionCore toInsert) throws Exception {
        return transactionRepository.save(toInsert);
    }

    @Override
    public TransactionCoreFailed createFailed(TransactionCoreFailed toInsert) throws Exception {
        return transactionFailedRepository.save(toInsert);
    }

    @Override
    public int updateRecordsWithCondition(boolean isSettled, int settledMethod, String originId, String merchantId,
                                          String terminalId, int batchNo) throws RuntimeException {
        return transactionRepository.updateRecordsWithCondition(true, settledMethod, originId,
                merchantId, terminalId, batchNo);
    }


    @Override
    public Page<TransactionCore> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return transactionRepository.findByDateTimeBetween(dateFormat.parse(dateFrom), dateFormat.parse(dateTo),
                pageable);

    }

    @Override
    public List<Object[]> getAllTransactionSummary(String dateFrom,
                                                   String dateTo,
                                                   String queryBy,
                                                   String whereClause,
                                                   String selectClause,
                                                   String groupByClause) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String jpql = "SELECT " + selectClause + " FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
                " WHERE " + whereClause + " GROUP BY " + groupByClause;

        Query query = entityManager.createQuery(jpql);

        if ((dateFrom != null && !dateFrom.isEmpty())
                && (dateTo != null && !dateTo.isEmpty())) {
            query.setParameter("fromDate", dateFormat.parse(dateFrom));
            query.setParameter("toDate", dateFormat.parse(dateTo));
        } else {
        }

        return query.getResultList();

    }

}
