package com.retail.productapi.domain;

/**
 * Domain class to hold description details of the product from Redsky API.
 */
public class ProductDescription {

    private String title;

    public ProductDescription() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ProductDescription = {" +
                "title='" + title + '\'' +
                '}';
    }
}
