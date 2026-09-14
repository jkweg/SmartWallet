package com.jkweg.smartwallet.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
