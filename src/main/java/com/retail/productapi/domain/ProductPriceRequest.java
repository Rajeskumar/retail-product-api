package com.retail.productapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This ProductPriceRequest class holds pricing data to be updated in datastore
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductPriceRequest {

    private double value;

    private String currency_code;

    public ProductPriceRequest() {
    }

    public ProductPriceRequest(double value, String currency_code) {
        this.value = value;
        this.currency_code = currency_code;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    @Override
    public String toString() {
        return "ProductPriceRequest{" +
                "value=" + value +
                ", currency_code='" + currency_code + '\'' +
                '}';
    }
}
