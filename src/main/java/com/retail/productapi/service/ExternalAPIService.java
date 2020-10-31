package com.retail.productapi.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface ExternalAPIService<T> {

    /**
     * Fetches external api response
     * @param apiURL
     * @return
     */
    @Async
    @Retryable(value = Exception.class, backoff = @Backoff(delay = 500))
    CompletableFuture<T> fetchAPIResponse (String apiURL, Class<T> responseType);
}
