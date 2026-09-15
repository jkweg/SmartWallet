package com.jkweg.smartwallet.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t where t.type =:type")
    BigDecimal getSumByType(TransactionType type);

    List<Transaction> findByType(TransactionType type);

    List<Transaction> findByCategory(TransactionCategory category);

    List<Transaction> findByTypeAndCategory(TransactionType type, TransactionCategory category);
}
