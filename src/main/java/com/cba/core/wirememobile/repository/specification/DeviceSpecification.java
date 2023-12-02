package com.cba.core.wirememobile.repository.specification;

import com.cba.core.wirememobile.model.Device;
import org.springframework.data.jpa.domain.Specification;

public interface DeviceSpecification {

    static Specification<Device> serialNoLikeAndDeviceTypeLike(String serialNo, String deviceType) throws Exception {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("serialNo")), "%" + serialNo.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("deviceType")), "%" + deviceType.toLowerCase() + "%")
                );
    }
}
