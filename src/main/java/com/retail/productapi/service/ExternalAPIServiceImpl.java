package com.retail.productapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class ExternalAPIServiceImpl<T> implements ExternalAPIService{

    private RestTemplate restTemplate;

    @Value("${external.api.conn.timeout}")
    private int externalAPIConnTimeout;

    @Value("${external.api.read.timeout}")
    private int externalAPIReadTimeout;

    private static final Logger logger = LoggerFactory.getLogger(ExternalAPIService.class);

    public ExternalAPIServiceImpl(RestTemplateBuilder builder) {
        builder.setConnectTimeout(Duration.ofSeconds(externalAPIConnTimeout));
        builder.setReadTimeout(Duration.ofSeconds(externalAPIReadTimeout));
        this.restTemplate = builder.build();
    }


    /**
     * Fetches external api response
     *
     * @param apiURL
     * @return
     */
    @Override
    public CompletableFuture<ResponseEntity<T>> fetchAPIResponse(String apiURL, Class responseType) throws RestClientException {

        logger.info("Fetching External Api Response");
        ResponseEntity<T> response = restTemplate.getForEntity(apiURL, responseType);

        return CompletableFuture.completedFuture(response);
    }
}
