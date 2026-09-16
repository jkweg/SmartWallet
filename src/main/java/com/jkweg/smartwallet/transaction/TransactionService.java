package com.jkweg.smartwallet.transaction;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.jkweg.smartwallet.transaction.TransactionSpecification.*;

@Service
class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository){
        this.repository = repository;
    }

    public Transaction addTransaction(TransactionRequest transactionRequest){

        Transaction transaction = new Transaction(
                transactionRequest.amount(),
                transactionRequest.type(),
                transactionRequest.category(),
                transactionRequest.date(),
                transactionRequest.description());

        return repository.save(transaction);
    }

    public List<Transaction> getAllTransactions(){

        return repository.findAll();

    }

    public Transaction getTransaction(Long id){
        return repository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public void deleteTransaction(Long id){
        getTransaction(id);
        repository.deleteById(id);
    }

    @Transactional
    public Transaction modifyTransaction(Long id, TransactionRequest transactionRequest){

        Transaction modifiedTransaction = getTransaction(id);

        modifiedTransaction.setAmount(transactionRequest.amount());
        modifiedTransaction.setCategory(transactionRequest.category());
        modifiedTransaction.setDate(transactionRequest.date());
        modifiedTransaction.setType(transactionRequest.type());
        modifiedTransaction.setDescription(transactionRequest.description());

        return modifiedTransaction;
    }

    public BigDecimal getBalance(){

        BigDecimal income = repository.getSumByType(TransactionType.INCOME);
        BigDecimal expense = repository.getSumByType(TransactionType.EXPENSE);

        return income.subtract(expense);
    }

    public TransactionSummary getSummary(){
        BigDecimal income = repository.getSumByType(TransactionType.INCOME);
        BigDecimal expense = repository.getSumByType(TransactionType.EXPENSE);

        BigDecimal balance = income.subtract(expense);

        return new TransactionSummary(income,expense,balance);
    }

    public List<Transaction> findTransactions(TransactionType type, TransactionCategory category, LocalDate from, LocalDate to){
        if(from != null && to != null && from.isAfter(to)){
            throw new InvalidDateRangeException();
        }
        Specification<Transaction> spec = Specification.allOf(hasType(type),hasCategory(category),dateFrom(from),dateTo(to));
        return repository.findAll(spec);
    }

}
