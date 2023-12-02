package com.cba.core.wirememobile.repository.specification;

import com.cba.core.wirememobile.model.User;
import org.springframework.data.jpa.domain.Specification;

public interface UserSpecification {
    static Specification<User> userNameLike(String userName) throws Exception {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("userName")), "%" + userName.toLowerCase() + "%");
    }
}
