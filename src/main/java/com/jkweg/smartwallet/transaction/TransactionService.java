package com.jkweg.smartwallet.transaction;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository){
        this.repository = repository;
    }

    public Transaction addTransaction(Transaction transaction){
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

    public Transaction modifyTransaction(Long id, Transaction transaction){
        Transaction modifiedTransaction = getTransaction(id);

        modifiedTransaction.setAmount(transaction.getAmount());
        modifiedTransaction.setCategory(transaction.getCategory());
        modifiedTransaction.setDate(transaction.getDate());
        modifiedTransaction.setType(transaction.getType());
        modifiedTransaction.setDescription(transaction.getDescription());

        return repository.save(modifiedTransaction);
    }

}
