package com.retail.productapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductAPIResponse {

    @JsonProperty("id")
    private int productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("current_price")
    private ProductPriceResponse productPriceResponse;

    public ProductAPIResponse() {
    }

    public ProductAPIResponse(int productId, String productName, ProductPriceResponse productPriceResponse) {
        this.productId = productId;
        this.productName = productName;
        this.productPriceResponse = productPriceResponse;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductPriceResponse getProductPriceResponse() {
        return productPriceResponse;
    }

    public void setProductPriceResponse(ProductPriceResponse productPriceResponse) {
        this.productPriceResponse = productPriceResponse;
    }
}
