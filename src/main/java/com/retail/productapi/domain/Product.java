package com.retail.productapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}
