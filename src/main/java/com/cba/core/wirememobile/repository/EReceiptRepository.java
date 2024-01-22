package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.EReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface EReceiptRepository extends JpaRepository<EReceipt, Integer>, JpaSpecificationExecutor<EReceipt> {

    Optional<EReceipt> findByTransactionCore_Id(Integer integer);

    Optional<EReceipt> findByTransactionCore_IdAndReceiptType(Integer integer, String receiptType);
}
