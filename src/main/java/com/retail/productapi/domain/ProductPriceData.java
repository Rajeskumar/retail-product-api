package com.retail.productapi.domain;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

/**
 * Domain class to holds pricing data from datastore
 */
@Table(value = "prod_price_table")
public class ProductPriceData implements Serializable {

    @PrimaryKey (value = "prod_id")
    private int productId;

    @Column (value = "prod_price")
    private String productPrice;

    public ProductPriceData(int productId, String productPrice) {
        this.productId = productId;
        this.productPrice = productPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "ProductPriceData ={" +
                "productId=" + productId +
                ", productPrice='" + productPrice + '\'' +
                '}';
    }
}
