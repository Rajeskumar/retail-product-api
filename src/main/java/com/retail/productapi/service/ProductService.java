package com.retail.productapi.service;

import com.retail.productapi.domain.Product;
import com.retail.productapi.domain.ProductAPIResponse;
import com.retail.productapi.domain.RedskyAPIProductData;
import com.retail.productapi.domain.ProductPriceData;

public interface ProductService {


    /**
     * Gets product detail by productId
     *
     * @param productId
     * @return @{ProductAPIResponse}
     */
    ProductAPIResponse getProductDetail(int productId);

    /**
     * Gets Product data from API by productId
     *
     * @param productId
     * @return @{RedskyAPIProductData}
     */
    Product getRedskyAPIProductData(int productId);

    /**
     * Gets Product pricing data from datastore by productId
     *
     * @param productId
     * @return @{ProductPriceData}
     */
    ProductPriceData getProductPriceData(int productId);
}
