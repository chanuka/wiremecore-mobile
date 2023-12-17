package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.DeviceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceConfigRepository extends JpaRepository<DeviceConfig, Integer>, JpaSpecificationExecutor<DeviceConfig> {

    Optional<DeviceConfig> findByDevice_Id(int deviceId);

}
