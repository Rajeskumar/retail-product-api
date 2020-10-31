package com.retail.productapi.service;

import com.retail.productapi.domain.ProductAPIResponse;

public interface ProductService {


    /**
     * Gets product detail by productId
     *
     * @param productId
     * @return @{ProductAPIResponse}
     */
    ProductAPIResponse getProductDetail(int productId) throws Exception;

}
