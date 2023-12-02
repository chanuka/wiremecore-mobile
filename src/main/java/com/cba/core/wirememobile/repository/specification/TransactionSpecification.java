package com.cba.core.wirememobile.repository.specification;

import com.cba.core.wirememobile.model.TransactionCore;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public interface TransactionSpecification {

    static Specification<TransactionCore> fromDateAndToDate(Date fromDate, Date toDate) throws Exception {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.between(root.get("dateTime"), fromDate, toDate),
                        criteriaBuilder.between(root.get("dateTime"), fromDate, toDate)
                );
    }

}
