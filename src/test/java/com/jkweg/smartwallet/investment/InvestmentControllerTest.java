package com.jkweg.smartwallet.investment;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(InvestmentController.class)
class InvestmentControllerTest {

    @MockitoBean InvestmentService service;

    @Autowired MockMvc mockMvc;

    @Autowired ObjectMapper objectMapper;

    @Test
    void shouldAddInvestment() throws Exception{

        // given

        InvestmentTransactionRequest request = new InvestmentTransactionRequest(
                "AAPL",
                InvestmentType.STOCK ,
                InvestmentOperationType.BUY,
                new BigDecimal("2") ,
                new BigDecimal("300"),
                LocalDate.of(2026,9,30)
                );

        InvestmentTransaction transaction = new InvestmentTransaction("AAPL",
                InvestmentType.STOCK ,
                InvestmentOperationType.BUY,
                new BigDecimal("2") ,
                new BigDecimal("300"),
                LocalDate.of(2026,9,30));

        when(service.addInvestmentTransaction(any(InvestmentTransactionRequest.class))).thenReturn(transaction);

        // when + then

        mockMvc.perform(
                        post("/investments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.symbol").value("AAPL"))
                .andExpect(jsonPath("$.investmentType").value("STOCK"))
                .andExpect(jsonPath("$.operationType").value("BUY"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.pricePerUnit").value(300));



    }

    @Test
    void shouldReturnBadRequestForBlankSymbol() throws Exception {

        // given

        InvestmentTransactionRequest request = new InvestmentTransactionRequest(
                " ",
                InvestmentType.STOCK ,
                InvestmentOperationType.BUY,
                new BigDecimal("2") ,
                new BigDecimal("300"),
                LocalDate.of(2026,9,30)
        );

        // when + then

        mockMvc.perform(
                        post("/investments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verify(service, never()).addInvestmentTransaction(any());

    }

    @Test
    void shouldReturnInvestmentTransaction() throws Exception {

        // given

        InvestmentTransaction transaction = new InvestmentTransaction("AAPL",
                InvestmentType.STOCK ,
                InvestmentOperationType.BUY,
                new BigDecimal("2") ,
                new BigDecimal("300"),
                LocalDate.of(2026,9,30));

        when(service.getInvestmentTransaction(1L)).thenReturn(transaction);

        // when + then

        mockMvc.perform(
                        get("/investments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("AAPL"))
                .andExpect(jsonPath("$.investmentType").value("STOCK"))
                .andExpect(jsonPath("$.operationType").value("BUY"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.pricePerUnit").value(300))
                .andExpect(jsonPath("$.date").value("2026-09-30"));


    }

    @Test
    void shouldReturnNotFoundWhenInvestmentTransactionDoesNotExist() throws Exception{

        // given

        when(service.getInvestmentTransaction(1L)).thenThrow(InvestmentTransactionNotFoundException.class);

        // when + then

        mockMvc.perform(
                    get("/investments/1")
                )
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldReturnInvestmentPositionSummary() throws Exception{

        // given

        InvestmentPositionSummary summary = new InvestmentPositionSummary(
                new BigDecimal("2"),
                new BigDecimal("200"),
                new BigDecimal("300"),
                new BigDecimal("600"),
                new BigDecimal("400"));

        when(service.getPositionSummary("AAPL")).thenReturn(summary);

        // when + then

        mockMvc.perform(
                get("/investments/AAPL/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentQuantity").value(2))
                .andExpect(jsonPath("$.netInvestedAmount").value(200))
                .andExpect(jsonPath("$.currentPrice").value(300))
                .andExpect(jsonPath("$.currentValue").value(600))
                .andExpect(jsonPath("$.profitLoss").value(400));
    }


}
