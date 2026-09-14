package com.jkweg.smartwallet.transaction;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service){
        this.service = service;
    }

    @PostMapping
    Transaction addTransaction(@RequestBody Transaction transaction){
        return service.addTransaction(transaction);
    }

    @GetMapping
    List<Transaction> getAllTransactions(){
        return service.getAllTransactions();
    }

    @GetMapping("/{id}")
    Transaction getTransaction(@PathVariable Long id){
        return service.getTransaction(id);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteTransaction(@PathVariable Long id){
        service.deleteTransaction(id);
    }

    @PutMapping("/{id}")
    Transaction modifyTransaction(@RequestBody Transaction transaction, @PathVariable Long id){
        return service.modifyTransaction(id,transaction);
    }

}
