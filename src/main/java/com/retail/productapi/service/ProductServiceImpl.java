package com.retail.productapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.productapi.dao.ProductPriceRepository;
import com.retail.productapi.domain.*;
import com.retail.productapi.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.cassandra.CassandraConnectionFailureException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This class fetches product details from RedskyAPI and pricing data from datastore by productId
 * and builds {@link ProductAPIResponse}
 */
@Service
public class ProductServiceImpl implements ProductService {

    private ProductPriceRepository productPriceRepository;

    private ObjectMapper objectMapper;

    private ExternalAPIService externalAPIService;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

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
     * Gets product detail by productId from Redsky API and datastore
     *
     * @param productId
     * @return @{ProductAPIResponse}
     */
    @Override
    @Cacheable(value = "productAPIResponseCache", key = "#productId", unless = "#result == null")
    public ProductAPIResponse getProductDetail(int productId){

        Product redskyProductData = null;
        ProductPriceData productPriceData = null;
        ProductAPIResponse productAPIResponse = null;

        try{
            //Fetch product data from Redsky API.
            CompletableFuture<RedskyAPIProductData> apiResponse = externalAPIService
                    .getAPIResponse(redskyAPIURLBase+productId+redskyAPIURLParams, RedskyAPIProductData.class);

            //Fetch pricing data from datastore.
            productPriceData = getProductPriceData(productId);

            RedskyAPIProductData redskyResponse = apiResponse.get();

            if(redskyResponse != null){
                redskyProductData = redskyResponse.getProduct();
            }

        }catch (InterruptedException | ExecutionException apiExce){
            logger.error("Redsky API Exception : "+apiExce.getMessage());
        }

        if(redskyProductData != null || productPriceData != null){
            productAPIResponse = buildProductAPIResponse(productId, redskyProductData, productPriceData);
        }
        return productAPIResponse;
    }

    /**
     * Updates product price data to datastore from the update request.
     *
     * @param productUpdateRequest
     * @return {@link ProductAPIResponse} updated ProductPrice data.
     * @throws Exception
     */
    @Override
    public ProductAPIResponse updateProductPriceData(ProductUpdateRequest productUpdateRequest) throws BadRequestException,
            CassandraConnectionFailureException{

        ProductPriceData updatePriceRequest = null;
        ProductPriceData updatedPriceData = null;

        try{
            String updatedPrice = objectMapper.writeValueAsString(productUpdateRequest.getProductPriceRequest());
            updatePriceRequest = new ProductPriceData(productUpdateRequest.getProductId(), updatedPrice);

            updatedPriceData = updatePriceDataToDatastore(updatePriceRequest);

        } catch (JsonProcessingException exc){
            logger.error("Json Processing Exception while parsing product Price request ", exc);
            throw new BadRequestException ("Parsing error while processing update price details");
        } catch (CassandraConnectionFailureException exc){
            logger.error("Cassandra Datastore Connection Exception : "+exc.getMessage());
            throw exc;
        }

        return buildProductAPIResponse(productUpdateRequest.getProductId(), null, updatedPriceData);
    }

    /**
     * Updates the Price data to datastore
     * @param newPriceData, update pricing request for the product.
     * @return
     */
    private ProductPriceData updatePriceDataToDatastore(ProductPriceData newPriceData) throws CassandraConnectionFailureException{

        ProductPriceData updatedPriceData = null;
        //Save to datastore
        updatedPriceData = productPriceRepository.save(newPriceData);

        return updatedPriceData;
    }

    /**
     * Gets Product pricing data from datastore by productId
     *
     * @param productId
     * @return {@link ProductPriceData}
     */
    public ProductPriceData getProductPriceData(int productId) {

        Optional<ProductPriceData> productPriceData = null;
        ProductPriceData priceData = null;

        try{
            logger.info("Getting pricing data from datastore, productId = {}", productId);
            productPriceData = productPriceRepository.findById(productId);

            if(productPriceData != null && productPriceData.isPresent()){
                priceData = productPriceData.get();
                logger.info("Product Price Data from Datastore, {}",priceData);
            }else{
                logger.info("Pricing Data not available in Datastore.");
            }
        }catch (CassandraConnectionFailureException exc){
            logger.error("Cassandra Datastore Connection Exception : "+exc.getMessage());
        }

        return priceData;
    }

    /**
     * Builds the ProductAPI response from ProductPrice data and Product data
     * @param productId
     * @param redskyProductData Product details fetched from redsky api
     * @param productPriceData Product pricing data fetched from datastore
     * @return {@link ProductAPIResponse}
     */
    public ProductAPIResponse buildProductAPIResponse(int productId, Product redskyProductData, ProductPriceData productPriceData) {

        ProductAPIResponse productAPIResponse = new ProductAPIResponse();
        productAPIResponse.setProductId(productId);
        //Build product name from redsky api product response.
        if(redskyProductData != null){
            if(redskyProductData.getProductItem() != null && redskyProductData.getProductItem().getProductDescription() != null){
                productAPIResponse.setProductName(redskyProductData.getProductItem().getProductDescription().getTitle());
            }else{
                productAPIResponse.setProductName("");
                logger.info("Product data not found in Redsky API for productId = {}", productId);
            }
        }

        ProductPriceResponse priceResponse = null;

        //Build pricing response from Pricing data from datastore
        if(productPriceData != null){
            try {
                priceResponse = objectMapper.readValue(productPriceData.getProductPrice(), ProductPriceResponse.class);
            } catch (JsonProcessingException ex) {
                logger.error("Error parsing pricing data: "+ex.getMessage());
            }
        }

        productAPIResponse.setProductPriceResponse(priceResponse);
        return productAPIResponse;
    }
}
