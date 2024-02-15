package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.Terminal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Integer>, JpaSpecificationExecutor<Terminal> {

//    Page<Terminal> findAllByMerchant_Id(int id, Pageable pageable);

    Optional<Terminal> findByTerminalId(String terminalId);

//    @Query("SELECT d FROM Terminal t inner join Device d on t.device.id =d.id WHERE t.terminalId = :terminalId and d.serialNo = :serialNo")
    Optional<Terminal> findByTerminalIdAndMerchant_MerchantIdAndDevice_SerialNo(String terminalId,String merchantId,String serialNo);

}
