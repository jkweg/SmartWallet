package com.jkweg.smartwallet.investment;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/investments")
class InvestmentController {

    private final InvestmentService service;

    InvestmentController(InvestmentService service){
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping

    InvestmentTransaction addInvestmentTransaction(@Valid @RequestBody InvestmentTransactionRequest request){
        return service.addInvestmentTransaction(request);
    }

    @GetMapping
    List<InvestmentTransaction> getAllInvestmentTransactions(){
        return service.getAllInvestmentTransactions();
    }

    @GetMapping("/{id}")
    InvestmentTransaction getInvestmentTransaction(@PathVariable Long id){
        return service.getInvestmentTransaction(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteInvestmentTransaction(@PathVariable Long id){
        service.deleteInvestmentTransaction(id);
    }

    @PutMapping("/{id}")
    InvestmentTransaction modifyInvestmentTransaction(@PathVariable Long id, @Valid @RequestBody InvestmentTransactionRequest request){
        return service.modifyInvestmentTransaction(id,request);
    }

    @GetMapping("/{symbol}/quantity")
    BigDecimal getCurrentQuantity(@PathVariable String symbol){
        return service.getCurrentQuantity(symbol);
    }

    @GetMapping("/{symbol}/net-invested")
    BigDecimal getNetInvestedAmount(@PathVariable String symbol){
        return service.getNetInvestedAmount(symbol);
    }

    @GetMapping("/{symbol}/current-price")
    BigDecimal getCurrentPrice(@PathVariable String symbol){
        return service.getCurrentPrice(symbol);
    }

    @GetMapping("/{symbol}/summary")
    InvestmentPositionSummary getPositionSummary(@PathVariable String symbol){
        return service.getPositionSummary(symbol);
    }

    @GetMapping("/types")
    List<InvestmentType> getAllInvestmentTypes(){
        return List.of(InvestmentType.values());
    }

    @GetMapping("/operation-types")
    List<InvestmentOperationType> getAllInvestmentOperationTypes(){
        return List.of(InvestmentOperationType.values());
    }

    @GetMapping("/portfolio-summary")
    InvestmentPortfolioSummary getPortfolioSummary(){
        return service.getInvestmentPortfolioSummary();
    }



}
