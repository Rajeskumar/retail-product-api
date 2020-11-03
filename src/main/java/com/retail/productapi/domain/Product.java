package com.retail.productapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain class to holds Product data from RedskyAPI
 */
public class Product {

    @JsonProperty(value = "item")
    private ProductItem productItem;

    public Product() {
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    @Override
    public String toString() {
        return "Product = { " + productItem +
                '}';
    }
}
