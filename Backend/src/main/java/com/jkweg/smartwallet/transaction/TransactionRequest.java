package com.jkweg.smartwallet.transaction;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

record TransactionRequest(@NotNull @Positive(message = "Amount must be greater than 0") BigDecimal amount,
                          @NotNull(message = "Transaction must have type") TransactionType type,
                          @NotNull(message = "Transaction must have category") TransactionCategory category,
                          @NotNull(message = "Transaction must have date") LocalDate date,
                          @NotBlank(message = "Transaction must have description") String description) {
}
