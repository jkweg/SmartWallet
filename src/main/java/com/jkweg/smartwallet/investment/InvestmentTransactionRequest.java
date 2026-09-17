package com.jkweg.smartwallet.investment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

record InvestmentTransactionRequest(@NotBlank(message = "Investment transaction must have a symbol") String symbol,
                                    @NotNull(message = "Investment transaction must have an investment type") InvestmentType investmentType,
                                    @NotNull(message = "Investment transaction must have an investment operation type") InvestmentOperationType operationType,
                                    @NotNull @Positive(message = "Investment quantity must be greater than 0") BigDecimal quantity,
                                    @NotNull @Positive(message = "Investment price per unit must be greater than 0") BigDecimal pricePerUnit,
                                    @NotNull(message = "Investment must have date") LocalDate date) {



}
