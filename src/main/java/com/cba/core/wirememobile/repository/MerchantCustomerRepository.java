package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.MerchantCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MerchantCustomerRepository extends JpaRepository<MerchantCustomer, Integer>, JpaSpecificationExecutor<MerchantCustomer> {
}
