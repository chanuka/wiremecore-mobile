package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.TransactionCore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionCore, Integer>, JpaSpecificationExecutor<TransactionCore> {

    Page<TransactionCore> findByDateTimeBetween(Date fromDate, Date toDate, Pageable pageable);

    @Modifying
    @Query("UPDATE TransactionCore e SET e.issettled = :isSettled , e.settledMethod = :settledMethod " +
            "WHERE e.originId = :originId and e.merchantId = :merchantId and e.terminalId= :terminalId and e.batchNo = :batchNo")
    int updateRecordsWithCondition(@Param("isSettled") boolean isSettled, @Param("settledMethod") int settledMethod,
                                   @Param("originId") String originId, @Param("merchantId") String merchantId,
                                   @Param("terminalId") String terminalId, @Param("batchNo") int batchNo);

}
