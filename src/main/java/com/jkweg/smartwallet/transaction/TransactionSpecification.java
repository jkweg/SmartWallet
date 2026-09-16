package com.jkweg.smartwallet.transaction;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

class TransactionSpecification {

    public static Specification<Transaction> hasType(TransactionType type){
        return (root, query, criteriaBuilder) -> {
            if ( type == null){
                return null;
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    public static Specification<Transaction> hasCategory(TransactionCategory category){
        return (root, query, criteriaBuilder) -> {

            if ( category == null){
                return null;
            }
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Transaction> dateFrom(LocalDate from){
        return (root, query, criteriaBuilder) -> {
            if (from == null){
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), from);
        };
    }

    public static Specification<Transaction> dateTo(LocalDate to){
        return (root, query, criteriaBuilder) -> {
            if(to == null){
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("date"), to);
        };
    }


}
