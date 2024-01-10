package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer>, JpaSpecificationExecutor<Device> {

    Optional<Device> findBySerialNo(String serialNo);

    @Query("SELECT d FROM Device d WHERE d.id = (SELECT t.device.id from Terminal t WHERE t.terminalId= :terminalId)")
    Optional<Device> findByTransactionTerminal(String terminalId);


}
