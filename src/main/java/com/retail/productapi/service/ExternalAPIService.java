package com.retail.productapi.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.HttpServerErrorException;

import java.util.concurrent.CompletableFuture;

public interface ExternalAPIService<T> {

    /**
     * Fetches external api response and returns completablefuture response
     *
     * @param apiURL
     * @return {@link CompletableFuture<T>}
     */
    @Async
    @Retryable(value = HttpServerErrorException.class, backoff = @Backoff(delay = 500))
    CompletableFuture<T> fetchAPIResponse (String apiURL, Class<T> responseType);
}
