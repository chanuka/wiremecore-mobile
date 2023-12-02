package com.cba.core.wirememobile.repository.specification;

import com.cba.core.wirememobile.model.MerchantCustomer;
import com.cba.core.wirememobile.model.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public interface MerchantCustomerSpecification {

    static Specification<MerchantCustomer> nameLikeAndStatusLike(String merchantCustomerName, String statusCode) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<MerchantCustomer, Status> join = root.join("status", JoinType.INNER);
            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(join.get("statusCode")), "%" + statusCode.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + merchantCustomerName.toLowerCase() + "%")
            );
        };
    }
}
