package com.jkweg.smartwallet.investment;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
class InvestmentService {

    private final InvestmentRepository repository;

    InvestmentService(InvestmentRepository repository){
        this.repository = repository;
    }


    public InvestmentTransaction addInvestmentTransaction(InvestmentTransactionRequest request){

        BigDecimal quantity = getCurrentQuantity(request.symbol());

        if (quantity.compareTo(request.quantity()) < 0 && request.operationType() == InvestmentOperationType.SELL){
            throw new InsufficientInvestmentQuantityException();
        }


        InvestmentTransaction investmentTransaction = new InvestmentTransaction(
                    request.symbol(),
                    request.investmentType(),
                    request.operationType(),
                    request.quantity(),
                    request.pricePerUnit(),
                    request.date());

        return repository.save(investmentTransaction);

    }

    public List<InvestmentTransaction> getAllInvestmentTransactions(){
        return repository.findAll();
    }

    public InvestmentTransaction getInvestmentTransaction(Long id){
        return repository.findById(id).orElseThrow( () -> new InvestmentTransactionNotFoundException(id));
    }

    public void deleteInvestmentTransaction(Long id){
        getInvestmentTransaction(id);
        repository.deleteById(id);
    }

    @Transactional
    public InvestmentTransaction modifyInvestmentTransaction(Long id, InvestmentTransactionRequest request){

        InvestmentTransaction transaction = getInvestmentTransaction(id);

        transaction.setSymbol(request.symbol());
        transaction.setInvestmentType(request.investmentType());
        transaction.setOperationType(request.operationType());
        transaction.setQuantity(request.quantity());
        transaction.setPricePerUnit(request.pricePerUnit());
        transaction.setDate(request.date());

        return transaction;
    }

    public BigDecimal getCurrentQuantity(String symbol){
        return repository.getCurrentQuantity(symbol);
    }

    public BigDecimal getNetInvestedAmount(String symbol){
        return repository.getNetInvestedAmount(symbol);
    }

}
