package com.retail.productapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductItem {

    @JsonProperty(value = "product_description")
    private ProductDescription productDescription;

    public ProductItem() {
    }

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(ProductDescription productDescription) {
        this.productDescription = productDescription;
    }

}
