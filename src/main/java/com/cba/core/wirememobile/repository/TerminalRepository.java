package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.Terminal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TerminalRepository extends JpaRepository<Terminal, Integer>, JpaSpecificationExecutor<Terminal> {
    Page<Terminal> findAllByMerchant_Id(int id, Pageable pageable);
}
