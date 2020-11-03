package com.retail.productapi.service;

import com.retail.productapi.exception.RestTemplateRetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface ExternalAPIService<T> {

    /**
     * Fetches external api response and returns completablefuture response
     *
     * @param apiURL
     * @return {@link CompletableFuture<T>}
     */
    @Async
    @Retryable(value = RestTemplateRetryException.class, backoff = @Backoff(delay = 1000))
    CompletableFuture<T> getAPIResponse(String apiURL, Class<T> responseType);
}
