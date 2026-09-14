package com.jkweg.smartwallet.transaction;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
class Transaction {

    private @Id @GeneratedValue Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionCategory category;

    private LocalDate date;

    private String description;

    protected Transaction(){}

    Transaction(BigDecimal amount, TransactionType type, LocalDate date, String description,TransactionCategory category){

        this.amount = amount;
        this.type = type;
        this.date = date;
        this.description = description;
        this.category = category;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }


//    @Override
//    public boolean equals(Object o){
//
//        if (this == o)
//            return true;
//        if (!(o instanceof Transaction))
//            return false;
//        Transaction transaction = (Transaction) o;
//        return Objects.equals(this.id,transaction.id) && Objects.equals(this.amount,transaction.amount)
//                && Objects.equals(this.date,transaction.date) && Objects.equals(this.description,transaction.description)
//                && Objects.equals(this.type,transaction.type) && Objects.equals(this.category, transaction.category);
//
//    }
//
//    @Override
//    public int hashCode(){
//        return Objects.hash(this.id, this.amount, this.type, this.date, this.description, this.category);
//    }

    @Override
    public String toString(){
        return "Transaction {" + "id=" + this.id + ", amount= " + this.amount + ", type= " + this.type +
                ", date= " + this.date + ", description= " + this.description + ", category= " + this.category;
    }


    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }
}
