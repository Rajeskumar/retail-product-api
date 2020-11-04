package com.retail.productapi.domain;

import java.io.Serializable;

/**
 * This ProductPriceResponse class holds pricing response to be returned in ProductAPIResponse
 */
public class ProductPriceResponse implements Serializable {

    private double value;

    private String currency_code;

    public ProductPriceResponse() {
    }

    public ProductPriceResponse(double value, String currency_code) {
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
        return "ProductPriceResponse = {" +
                "value=" + value +
                ", currency_code='" + currency_code + '\'' +
                '}';
    }
}
