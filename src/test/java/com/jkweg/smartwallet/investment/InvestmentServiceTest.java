package com.jkweg.smartwallet.investment;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceTest {

    @Mock InvestmentRepository repository;
    @Mock TwelveDataClient twelveDataClient;
    @InjectMocks InvestmentService service;

    @Test
    void shouldAddBuyInvestmentTransaction(){
        // given

        InvestmentTransactionRequest request = new InvestmentTransactionRequest(
                "AAPL",
                InvestmentType.STOCK,
                InvestmentOperationType.BUY,
                new BigDecimal("1.5"),
                new BigDecimal("200"),
                LocalDate.of(2026, 9, 19)
        );

        //when

        when(repository.getCurrentQuantity("AAPL")).thenReturn(BigDecimal.ZERO);

        service.addInvestmentTransaction(request);

        //then

        ArgumentCaptor<InvestmentTransaction> captor = ArgumentCaptor.forClass(InvestmentTransaction.class);
        verify(repository).save(captor.capture());
        InvestmentTransaction savedTransaction = captor.getValue();

        assertEquals("AAPL",savedTransaction.getSymbol());
        assertEquals(InvestmentOperationType.BUY, savedTransaction.getOperationType());
        assertEquals(new BigDecimal("1.5"), savedTransaction.getQuantity());
        assertEquals(InvestmentType.STOCK, savedTransaction.getInvestmentType());
        assertEquals(new BigDecimal("200"), savedTransaction.getPricePerUnit());
        assertEquals(LocalDate.of(2026,9,19), savedTransaction.getDate());

    }


    @Test
    void shouldThrowWhenSellingMoreThanOwned(){


        //given

        InvestmentTransactionRequest request = new InvestmentTransactionRequest(
                "AAPL",
                InvestmentType.STOCK,
                InvestmentOperationType.SELL,
                new BigDecimal("3"),
                new BigDecimal("200"),
                LocalDate.of(2026, 9, 19)
        );

        when(repository.getCurrentQuantity("AAPL")).thenReturn(new BigDecimal("2"));

        // when + then

        assertThrows(
                InsufficientInvestmentQuantityException.class,
                () -> service.addInvestmentTransaction(request));

        verify(repository, never()).save(any(InvestmentTransaction.class));

    }

    @Test
    void shouldReturnPositionSummary(){

        // given

        when(repository.getCurrentQuantity("AAPL")).thenReturn(new BigDecimal("2"));
        when(repository.getNetInvestedAmount("AAPL")).thenReturn(new BigDecimal("200"));
        when(twelveDataClient.getCurrentPrice("AAPL")).thenReturn(new BigDecimal("300"));

        //when

        InvestmentPositionSummary summary = service.getPositionSummary("AAPL");

        // then

        assertEquals(new BigDecimal("300"), summary.currentPrice());
        assertEquals(new BigDecimal("200"), summary.netInvestedAmount());
        assertEquals(new BigDecimal("2"), summary.currentQuantity());
        assertEquals(new BigDecimal("600.00"), summary.currentValue());
        assertEquals(new BigDecimal("400.00"), summary.profitLoss());
    }

    @Test
    void shouldReturnInvestmentTransactionWhenExists() {

        // given

        InvestmentTransaction transaction = new InvestmentTransaction(
                "AAPL",
                InvestmentType.STOCK,
                InvestmentOperationType.BUY,
                new BigDecimal("3"),
                new BigDecimal("200"),
                LocalDate.of(2026,9,19));

        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        // when

        InvestmentTransaction result = service.getInvestmentTransaction(1L);

        // then

        assertEquals(transaction, result);
    }

    @Test
    void shouldThrowWhenInvestmentTransactionDoesNotExist() {

        // given

        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when + then

        assertThrows(InvestmentTransactionNotFoundException.class, () -> service.getInvestmentTransaction(1L));
    }

    @Test
    void shouldModifyInvestmentTransaction() {
        // given

        InvestmentTransaction transaction = new InvestmentTransaction(
                "AAPL",
                InvestmentType.STOCK,
                InvestmentOperationType.BUY,
                new BigDecimal("3"),
                new BigDecimal("200"),
                LocalDate.of(2026,9,19));

        InvestmentTransactionRequest request = new InvestmentTransactionRequest(
                "AAPL",
                InvestmentType.STOCK,
                InvestmentOperationType.BUY,
                new BigDecimal("5"),
                new BigDecimal("200"),
                LocalDate.of(2026,9,19));

        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        // when

        InvestmentTransaction result = service.modifyInvestmentTransaction(1L, request);

        // then

        assertEquals(new BigDecimal("5"), result.getQuantity());
        assertEquals("AAPL", result.getSymbol());
        assertEquals(new BigDecimal("200"), result.getPricePerUnit());
        assertEquals(InvestmentOperationType.BUY, result.getOperationType());
        assertEquals(InvestmentType.STOCK, result.getInvestmentType());
        assertEquals(LocalDate.of(2026,9,19), result.getDate());

        verify(repository, never()).save(any());

    }

    @Test
    void shouldDeleteInvestmentTransaction(){

        // given

        InvestmentTransaction transaction = new InvestmentTransaction(
                "AAPL",
                InvestmentType.STOCK,
                InvestmentOperationType.BUY,
                new BigDecimal("3"),
                new BigDecimal("200"),
                LocalDate.of(2026,9,19));

        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        // when

        service.deleteInvestmentTransaction(1L);

        // then

        verify(repository).deleteById(1L);

    }

    @Test
    void shouldThrowWhenInvestmentTransactionDoesNotExistWhileDelete(){

        // given

        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when + then


        assertThrows(InvestmentTransactionNotFoundException.class , () -> service.deleteInvestmentTransaction(1L));
        verify(repository, never()).deleteById(1L);

    }
}
