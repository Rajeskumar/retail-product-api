package com.retail.productapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.productapi.dao.ProductPriceRepository;
import com.retail.productapi.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductPriceRepository productPriceRepository;

    private ObjectMapper objectMapper;

    private ExternalAPIService externalAPIService;

    private static final Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());

    @Autowired
    public ProductServiceImpl(ProductPriceRepository productPriceRepository, ObjectMapper objectMapper, ExternalAPIService externalAPIService) {
        this.productPriceRepository = productPriceRepository;
        this.objectMapper = objectMapper;
        this.externalAPIService = externalAPIService;
    }

    @Value("${redsky.api.url.base}")
    private String redskyAPIURLBase;

    @Value("${redsky.api.url.params}")
    private String redskyAPIURLParams;

    /**
     * Gets Product pricing data from datastore by productId
     *
     * @param productId
     * @return @{ProductPriceData}
     */
    @Override
    public ProductPriceData getProductPriceData(int productId) {
        Optional<ProductPriceData> productPriceData =  productPriceRepository.findById(productId);

        if(productPriceData.isPresent()){
            return productPriceData.get();
        }else{
            logger.log(Level.WARNING, "Pricing Data not available in Datastore.");
            return null;
        }
    }

    /**
     * Gets Product data from API by productId
     *
     * @param productId
     * @return @{RedskyAPIProductData}
     */
    @Override
    public Product getRedskyAPIProductData(int productId) {

        Product productData = null;
        try{
            ResponseEntity<RedskyAPIProductData> redskyAPIResponse = externalAPIService.fetchAPIResponse(redskyAPIURLBase+productId+redskyAPIURLParams, RedskyAPIProductData.class);

            if(redskyAPIResponse.getStatusCode().is2xxSuccessful()){
                productData = redskyAPIResponse.getBody().getProduct();
            }else if (!redskyAPIResponse.hasBody()){
                logger.log(Level.WARNING, "Empty response from Redsky API");
            }
        }catch (RestClientException restExc){
            logger.log(Level.SEVERE, "Redsky API Exception : "+restExc.getMessage());
        }

        return productData;
    }

    /**
     * Gets product detail by productId
     *
     * @param productId
     * @return @{ProductAPIResponse}
     */
    @Override
    public ProductAPIResponse getProductDetail(int productId) {

        Product redskyProductData = getRedskyAPIProductData(productId);
        ProductPriceData productPriceData = getProductPriceData(productId);

        if(redskyProductData == null && productPriceData == null){
            return null;
        }
        return buildProductAPIResponse(productId, redskyProductData, productPriceData);
    }

    /**
     * Builds the ProductAPI response from ProductPrice data and Product data
     * @param productId
     * @param redskyProductData
     * @param productPriceData
     * @return productAPIResponse
     */
    public ProductAPIResponse buildProductAPIResponse(int productId, Product redskyProductData, ProductPriceData productPriceData) {

        ProductAPIResponse productAPIResponse = new ProductAPIResponse();
        productAPIResponse.setProductId(productId);
        if(redskyProductData != null){
            if(redskyProductData.getProductItem() != null && redskyProductData.getProductItem().getProductDescription() != null){
                productAPIResponse.setProductName(redskyProductData.getProductItem().getProductDescription().getTitle());
            }else{
                productAPIResponse.setProductName("");
                logger.log(Level.WARNING, "Product Item data not found");
            }
        }else{
            logger.log(Level.WARNING, "Product not found in Redsky API");
        }


        ProductPriceResponse priceResponse = null;

        if(productPriceData != null){
            try {
                priceResponse = objectMapper.readValue(productPriceData.getProductPrice(), ProductPriceResponse.class);
            } catch (JsonProcessingException ex) {
                logger.log(Level.SEVERE, "Error parsing pricing data: "+ex.getMessage());
            }
        }

        productAPIResponse.setProductPriceResponse(priceResponse);
        return productAPIResponse;
    }
}
