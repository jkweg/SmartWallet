package com.jkweg.smartwallet.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock TransactionRepository repository;
    @InjectMocks TransactionService service;

    @Test
    void shouldReturnSummary(){

        // given

        when(repository.getSumByType(TransactionType.INCOME)).thenReturn(new BigDecimal("5000"));
        when(repository.getSumByType(TransactionType.EXPENSE)).thenReturn(new BigDecimal("2000"));

        // when

        TransactionSummary summary = service.getSummary();

        // then

        assertEquals( new BigDecimal("5000") , summary.totalIncome());
        assertEquals( new BigDecimal("2000") , summary.totalExpenses());
        assertEquals( new BigDecimal("3000") , summary.balance());

    }

    @Test
    void shouldThrowWhenDateRangeIsInvalid() {

        // given

        LocalDate from  = LocalDate.of(2026,9,30);
        LocalDate to = LocalDate.of(2025,10,1);

        // when + then

        assertThrows(InvalidDateRangeException.class, () -> service.findTransactions(null,null, from,to));
        verify(repository, never()).findAll(any(Specification.class));

    }

    @Test
    void shouldReturnSpecificTransactions(){

        // given

        Transaction transaction = new Transaction(
                new BigDecimal("120"),
                TransactionType.EXPENSE,
                TransactionCategory.FOOD,
                LocalDate.of(2026, 9, 15),
                "Groceries"
        );

        List<Transaction> expectedTransactions = List.of(transaction);

        when(repository.findAll(any(Specification.class))).thenReturn(expectedTransactions);

        // when

        List<Transaction> result = service.findTransactions(TransactionType.EXPENSE,
                TransactionCategory.FOOD ,
                LocalDate.of(2026,9,1) ,
                LocalDate.of(2026,9,30));

        // then

        assertEquals(expectedTransactions, result);

    }

}
