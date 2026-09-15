package com.jkweg.smartwallet.transaction;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    public List<Transaction> findByType(TransactionType type){
        return repository.findByType(type);
    }

    public List<Transaction> findByCategory(TransactionCategory category){
        return repository.findByCategory(category);
    }

    public List<Transaction> findByTypeAndCategory(TransactionType type, TransactionCategory category){
        return repository.findByTypeAndCategory(type, category);
    }

}
