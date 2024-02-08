package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.TransactionCoreFailed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionFailedRepository extends JpaRepository<TransactionCoreFailed, Integer>, JpaSpecificationExecutor<TransactionCoreFailed> {

}
