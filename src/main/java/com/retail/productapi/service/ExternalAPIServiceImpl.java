package com.retail.productapi.service;

import com.retail.productapi.exception.RestTemplateRetryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * This class is an generic implementation to retrieve response from external api
 * @param <T>
 */
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
     * Fetches external api response and returns completablefuture response
     *
     * @param apiURL
     * @return {@link CompletableFuture<T>}
     */
    @Override
    public CompletableFuture<T> getAPIResponse(String apiURL, Class responseType) {

        logger.info("Fetching External Api Response from api = {}", apiURL);
        ResponseEntity<T> response = null;
        T responseBody = null;
        try{
            response = restTemplate.getForEntity(apiURL, responseType);
            responseBody = response.getBody();
        }catch (HttpClientErrorException ex){
            if(HttpStatus.NOT_FOUND.equals(ex.getStatusCode())){
                logger.info("External Api Error, Resource not found");
            }else{
                logger.info("External Api Error = {}. Retrying, url = {}", ex, apiURL);
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "External API call failed.");
            }
        }catch (Exception ex){
            logger.error("External API throws exception, url={}, error={}", apiURL, ex.getMessage());
            throw new RestTemplateRetryException("External API call failed. Retry enabled. Failed after retry attempts");
        }

        logger.info("Fetched response={}", responseBody);
        return CompletableFuture.completedFuture(responseBody);
    }
}
