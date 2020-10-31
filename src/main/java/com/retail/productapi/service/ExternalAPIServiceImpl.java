package com.retail.productapi.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalAPIServiceImpl<T> implements ExternalAPIService{

    private RestTemplate restTemplate;

    public ExternalAPIServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    /**
     * Fetches external api response
     *
     * @param apiURL
     * @return
     */
    @Override
    public ResponseEntity fetchAPIResponse(String apiURL, Class responseType) throws RestClientException {

        ResponseEntity<T> response = restTemplate.getForEntity(apiURL, responseType);

        return response;
    }
}
