package com.jkweg.smartwallet.investment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
class TwelveDataClient {

    private final String apiKey;
    private final String baseUrl;

    private final RestClient restClient;

    TwelveDataClient(@Value("${twelvedata.api.key}") String apiKey, @Value("${twelvedata.api.base-url}") String baseUrl){
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    BigDecimal getCurrentPrice(String symbol){

        String price;

        TwelveDataPriceResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/price").
                        queryParam("symbol",symbol).
                        queryParam("apikey",apiKey).
                        build())
                .retrieve()
                .body(TwelveDataPriceResponse.class);


        return new BigDecimal(response.price());

    }

}
