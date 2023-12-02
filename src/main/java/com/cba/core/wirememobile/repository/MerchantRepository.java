package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MerchantRepository extends JpaRepository<Merchant, Integer>, JpaSpecificationExecutor<Merchant> {

    Page<Merchant> findAllByMerchantCustomer_Id(int id, Pageable pageable);

}
