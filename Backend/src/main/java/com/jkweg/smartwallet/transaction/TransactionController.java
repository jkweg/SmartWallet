package com.jkweg.smartwallet.transaction;


import jakarta.validation.Valid;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/transactions")
class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service){
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    Transaction addTransaction(@Valid @RequestBody TransactionRequest transactionRequest){
        return service.addTransaction(transactionRequest);
    }

    @GetMapping
    List<Transaction> getAllTransactions(@RequestParam(required = false) TransactionType type,
                                         @RequestParam(required = false) TransactionCategory category,
                                         @RequestParam(required = false) LocalDate from,
                                         @RequestParam(required = false) LocalDate to){

        return service.findTransactions(type,category,from,to);
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
    Transaction modifyTransaction(@Valid @RequestBody TransactionRequest transactionRequest, @PathVariable Long id){
        return service.modifyTransaction(id,transactionRequest);
    }

    @GetMapping("/balance")
    BigDecimal getBalance(){
        return service.getBalance();
    }

    @GetMapping("/summary")
    TransactionSummary getSummary(){
        return service.getSummary();
    }

    @GetMapping("/types")
    List<TransactionType> getAllTransactionTypes(){
        return List.of(TransactionType.values());
    }

    @GetMapping("/categories")
    List<TransactionCategory> getAllTransactionCategories(){
        return List.of(TransactionCategory.values());
    }


}
