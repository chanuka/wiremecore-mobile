package com.cba.core.wirememobile.repository.specification;

import com.cba.core.wirememobile.model.Merchant;
import com.cba.core.wirememobile.model.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public interface MerchantSpecification {

    static Specification<Merchant> nameLikeAndStatusLike(String merchantName, String statusCode) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<Merchant, Status> join = root.join("status", JoinType.INNER);
            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(join.get("statusCode")), "%" + statusCode.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + merchantName.toLowerCase() + "%")
            );
        };
    }
}
