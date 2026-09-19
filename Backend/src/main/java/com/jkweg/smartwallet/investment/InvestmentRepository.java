package com.jkweg.smartwallet.investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

interface InvestmentRepository extends JpaRepository<InvestmentTransaction, Long> {

    @Query("SELECT COALESCE(SUM(CASE WHEN it.operationType = com.jkweg.smartwallet.investment.InvestmentOperationType.BUY THEN it.quantity ELSE (0 - it.quantity) END ),0) FROM InvestmentTransaction as it where it.symbol =:symbol")
    BigDecimal getCurrentQuantity(String symbol);

    @Query("SELECT COALESCE(SUM(CASE WHEN it.operationType = com.jkweg.smartwallet.investment.InvestmentOperationType.BUY THEN it.quantity * it.pricePerUnit ELSE (0 - it.quantity * it.pricePerUnit) END ),0) FROM InvestmentTransaction as it where it.symbol =:symbol")
    BigDecimal getNetInvestedAmount(String symbol);
}
