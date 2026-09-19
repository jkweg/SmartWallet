package com.jkweg.smartwallet.investment;

import java.math.BigDecimal;

record InvestmentPositionSummary(BigDecimal currentQuantity,
                                 BigDecimal netInvestedAmount,
                                 BigDecimal currentPrice,
                                 BigDecimal currentValue,
                                 BigDecimal profitLoss) {
}
