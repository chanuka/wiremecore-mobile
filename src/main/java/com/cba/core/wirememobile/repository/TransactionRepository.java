package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.TransactionCore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionCore, Integer>, JpaSpecificationExecutor<TransactionCore> {

    Page<TransactionCore> findByDateTimeBetween(Date fromDate, Date toDate, Pageable pageable);

    @Modifying
    @Query("UPDATE TransactionCore e SET e.issettled = :isSettled , e.settledMethod = :settledMethod " +
            "WHERE e.originId = :originId and e.merchantId = :merchantId and e.terminalId= :terminalId and e.batchNo = :batchNo")
    int updateRecordsWithCondition(@Param("isSettled") boolean isSettled, @Param("settledMethod") int settledMethod,
                                   @Param("originId") String originId, @Param("merchantId") String merchantId,
                                   @Param("terminalId") String terminalId, @Param("batchNo") int batchNo);

    @Query("SELECT e FROM TransactionCore e inner join Terminal t on e.terminalId=t.terminalId inner join Device d on t.device.id=d.id " +
            "WHERE d.serialNo = :serialNo AND e.rrn = :rrn AND e.invoiceNo = :invoiceNo AND e.traceNo = :traceNo AND e.dateTime= :dateTime")
    Optional<TransactionCore> findByRrnAndInvoiceNoAndTraceNo(String serialNo, String rrn, Integer invoiceNo, Integer traceNo,Date dateTime);

}
