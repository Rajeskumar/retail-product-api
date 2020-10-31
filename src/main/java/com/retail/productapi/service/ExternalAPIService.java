package com.retail.productapi.service;

import org.springframework.http.ResponseEntity;

public interface ExternalAPIService<T> {

    /**
     * Fetches external api response
     * @param apiURL
     * @return
     */
    ResponseEntity<T> fetchAPIResponse (String apiURL, Class<T> responseType);
}
