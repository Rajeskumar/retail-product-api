package com.retail.productapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class holds request object to update Product data specifically Price data at the moment.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductUpdateRequest {

    @JsonProperty("id")
    private int productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("current_price")
    private ProductPriceRequest productPriceRequest;

    public ProductUpdateRequest() {
    }

    public ProductUpdateRequest(int productId, String productName, ProductPriceRequest productPriceRequest) {
        this.productId = productId;
        this.productName = productName;
        this.productPriceRequest = productPriceRequest;
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

    public ProductPriceRequest getProductPriceRequest() {
        return productPriceRequest;
    }

    public void setProductPriceRequest(ProductPriceRequest productPriceRequest) {
        this.productPriceRequest = productPriceRequest;
    }

    @Override
    public String toString() {
        return "ProductUpdateRequest{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPriceRequest=" + productPriceRequest +
                '}';
    }
}
