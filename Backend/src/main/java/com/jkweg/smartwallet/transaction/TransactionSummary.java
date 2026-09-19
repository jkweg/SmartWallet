package com.jkweg.smartwallet.transaction;

import java.math.BigDecimal;

record TransactionSummary(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal balance) {
}
