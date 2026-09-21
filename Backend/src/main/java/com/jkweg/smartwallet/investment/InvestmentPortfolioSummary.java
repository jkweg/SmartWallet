package com.jkweg.smartwallet.investment;

import java.math.BigDecimal;

record InvestmentPortfolioSummary(BigDecimal totalInvested,
                                  BigDecimal currentValue,
                                  BigDecimal profitLoss,
                                  int openPositions) {
}
