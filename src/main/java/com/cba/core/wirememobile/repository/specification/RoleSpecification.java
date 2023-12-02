package com.cba.core.wirememobile.repository.specification;

import com.cba.core.wirememobile.model.Role;
import org.springframework.data.jpa.domain.Specification;

public interface RoleSpecification {
    static Specification<Role> roleNameLike(String roleName) throws Exception {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("roleName")), "%" + roleName.toLowerCase() + "%");
    }
}
