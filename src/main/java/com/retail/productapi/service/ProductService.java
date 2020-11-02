package com.retail.productapi.service;

import com.retail.productapi.domain.ProductAPIResponse;
import com.retail.productapi.domain.ProductUpdateRequest;
import com.retail.productapi.exception.BadRequestException;
import org.springframework.data.cassandra.CassandraConnectionFailureException;

/**
 *
 */
public interface ProductService {


    /**
     * Gets product detail by productId from Redsky API and datastore
     *
     * @param productId
     * @return {@link ProductAPIResponse}
     */
    ProductAPIResponse getProductDetail(int productId) throws Exception;

    /**
     * Updates product price data to datastore from the update request.
     * @param productUpdateRequest
     * @return {@link ProductAPIResponse} updated ProductPrice data.
     * @throws Exception
     */
    ProductAPIResponse updateProductPriceData (ProductUpdateRequest productUpdateRequest)
            throws BadRequestException, CassandraConnectionFailureException;

}
