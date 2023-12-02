package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.DeviceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeviceModelRepository extends JpaRepository<DeviceModel, Integer>, JpaSpecificationExecutor<DeviceModel> {
}
