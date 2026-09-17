package com.jkweg.smartwallet.investment;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
class InvestmentTransaction {

    private @Id @GeneratedValue Long id;

    private String symbol;

    @Enumerated(EnumType.STRING)
    private InvestmentType investmentType;

    @Enumerated(EnumType.STRING)
    private  InvestmentOperationType operationType;

    private BigDecimal quantity;

    private BigDecimal pricePerUnit;

    private LocalDate date;

    protected InvestmentTransaction(){}

    InvestmentTransaction (String symbol, InvestmentType type, InvestmentOperationType operationType, BigDecimal quantity, BigDecimal price, LocalDate date){
        this.symbol = symbol;
        this.investmentType = type;
        this.operationType = operationType;
        this.quantity = quantity;
        this.pricePerUnit = price;
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public InvestmentType getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(InvestmentType investmentType) {
        this.investmentType = investmentType;
    }

    public InvestmentOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(InvestmentOperationType operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }
}
