package com.retail.productapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain class to hold Item details of the product from Redsky API.
 */
public class ProductItem {

    @JsonProperty(value = "product_description")
    private ProductDescription productDescription;

    public ProductItem() {
    }

    public ProductItem(ProductDescription productDescription) {
        this.productDescription = productDescription;
    }

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(ProductDescription productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public String toString() {
        return "ProductItem = {" + productDescription +
                '}';
    }
}
