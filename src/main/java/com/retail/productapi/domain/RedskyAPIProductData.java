package com.retail.productapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class holds product data from Redsky API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedskyAPIProductData {

    private Product product;

    public RedskyAPIProductData() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "RedskyAPIProductData = { " + product +
                '}';
    }
}
