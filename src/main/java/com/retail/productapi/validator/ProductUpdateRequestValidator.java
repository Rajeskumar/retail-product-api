package com.retail.productapi.validator;

import com.retail.productapi.domain.ProductUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ProductUpdateRequestValidator {

    Logger logger = LoggerFactory.getLogger(ProductUpdateRequestValidator.class);

    /**
     * Implements the validation logic for {@link ProductUpdateRequest}.
     * @param updateRequest   productUpdateRequest object to validate
     * @param productId
     * @return {@code false} if {@code value} does not pass the constraint
     */
    public boolean isValidRequest(ProductUpdateRequest updateRequest, int productId) {

        if(updateRequest != null
                && updateRequest.getProductId() > 0
                && updateRequest.getProductPriceRequest()!=null
                && updateRequest.getProductName()!=null
                && updateRequest.getProductPriceRequest().getValue() > 0.0
                && !StringUtils.isEmpty(updateRequest.getProductPriceRequest().getCurrency_code())){
            if(updateRequest.getProductId() == productId){
                return true;
            }else{
                logger.info("Product Id mismatch in the request body, Id={}, RequestBody Id={} ", productId, updateRequest.getProductId());
                return false;
            }
        }else{
            return false;
        }
    }
}
