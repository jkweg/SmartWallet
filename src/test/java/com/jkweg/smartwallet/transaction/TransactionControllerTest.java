package com.jkweg.smartwallet.transaction;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @MockitoBean
    TransactionService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnTransactionSummary() throws Exception{

        // given

        TransactionSummary summary = new TransactionSummary(new BigDecimal("5000") , new BigDecimal("2000") , new BigDecimal("3000"));

        when(service.getSummary()).thenReturn(summary);

        // when + then

        mockMvc.perform(get("/transactions/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(5000))
                .andExpect(jsonPath("$.totalExpenses").value(2000))
                .andExpect(jsonPath("$.balance").value(3000));
    }

    @Test
    void shouldAddTransactions() throws Exception{

        // givem

        TransactionRequest request = new TransactionRequest(
                new BigDecimal("500"),
                TransactionType.EXPENSE,
                TransactionCategory.FOOD,
                LocalDate.of(2026,9,30),
                "Picka");

        Transaction transaction = new Transaction(
                new BigDecimal("500"),
                TransactionType.EXPENSE,
                TransactionCategory.FOOD,
                LocalDate.of(2026,9,30),
                "Picka");

        when(service.addTransaction(request)).thenReturn(transaction);

        // when + then

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.type").value("EXPENSE"))
                .andExpect(jsonPath("$.category").value("FOOD"))
                .andExpect(jsonPath("$.date").value("2026-09-30"))
                .andExpect(jsonPath("$.description").value("Picka"));

    }

    @Test
    void shouldNotAddTransaction() throws Exception{

        // given

        TransactionRequest request = new TransactionRequest(
                new BigDecimal("-500"),
                TransactionType.EXPENSE,
                TransactionCategory.FOOD,
                LocalDate.of(2026,9,30),
                "Picka");

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service,never()).addTransaction(any());

    }
}
