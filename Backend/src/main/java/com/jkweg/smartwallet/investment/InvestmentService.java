package com.jkweg.smartwallet.investment;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
class InvestmentService {

    private final InvestmentRepository repository;
    private final TwelveDataClient twelveDataClient;

    InvestmentService(InvestmentRepository repository, TwelveDataClient twelveDataClient){
        this.repository = repository;
        this.twelveDataClient = twelveDataClient;
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

    public BigDecimal getCurrentPrice(String symbol){
        return twelveDataClient.getCurrentPrice(symbol);
    }

    public InvestmentPositionSummary getPositionSummary( String symbol ) {

        BigDecimal currentQuantity = getCurrentQuantity(symbol);
        BigDecimal netInvestedAmount = getNetInvestedAmount(symbol);
        BigDecimal currentPrice = getCurrentPrice(symbol);
        BigDecimal currentValue = currentQuantity.multiply(currentPrice);
        BigDecimal profitLoss = currentValue.subtract(netInvestedAmount);

        return new InvestmentPositionSummary(
                currentQuantity,
                netInvestedAmount,
                currentPrice,
                currentValue.setScale(2, RoundingMode.HALF_UP),
                profitLoss.setScale(2, RoundingMode.HALF_UP));

    }

    public Set<String> getAllInvestedSymbols(){

        Set<String> symbols = new HashSet<>();

        List<InvestmentTransaction> transactions = repository.findAll();
        for( InvestmentTransaction transaction : transactions){
            symbols.add(transaction.getSymbol());
        }

        return symbols;
    }

    public InvestmentPortfolioSummary getInvestmentPortfolioSummary(){

        Set<String> symbols = getAllInvestedSymbols();
        BigDecimal totalInvested = BigDecimal.ZERO;
        BigDecimal currentValue = BigDecimal.ZERO;
        int openPositions = 0;

        for( String symbol : symbols){
            BigDecimal currentQuantity = getCurrentQuantity(symbol);
            if(currentQuantity.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }

            openPositions += 1;
            BigDecimal netInvestedAmount = getNetInvestedAmount(symbol);
            BigDecimal currentPrice = getCurrentPrice(symbol);
            BigDecimal positionValue = currentQuantity.multiply(currentPrice);

            totalInvested = totalInvested.add(netInvestedAmount);
            currentValue = currentValue.add(positionValue);
        }

        BigDecimal profitLoss = currentValue.subtract(totalInvested);

        return new InvestmentPortfolioSummary(totalInvested.setScale(2, RoundingMode.HALF_UP), currentValue.setScale(2, RoundingMode.HALF_UP), profitLoss.setScale(2, RoundingMode.HALF_UP), openPositions);

    }

}
