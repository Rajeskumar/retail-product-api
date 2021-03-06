package com.retail.productapi.controller;

import com.retail.productapi.domain.ProductAPIResponse;
import com.retail.productapi.domain.ProductUpdateRequest;
import com.retail.productapi.exception.BadRequestException;
import com.retail.productapi.service.ProductService;
import com.retail.productapi.validator.ProductUpdateRequestValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A Controller class for Retail Product API end point.
 */
@RestController
@RequestMapping("/api/v1")
@Api(value = "/api/v1", description = "Retail Product API end point which aggregates product details from multiple sources.")
public class ProductAPIController {

    private static final Logger logger = LoggerFactory.getLogger(ProductAPIController.class);

    ProductService productService;

    ProductUpdateRequestValidator validator;

    @Autowired
    public ProductAPIController(ProductService productService, ProductUpdateRequestValidator validator) {
        this.productService = productService;
        this.validator = validator;
    }

    @GetMapping(path = "/")
    @ResponseBody
    String welcome(){
        return "Welcome to ProductAPI";
    }


    /**
     * Get Product endpoint to retrieve Product details from Redsky API and datastore
     * @param productId, Unique ID for the product
     * @return {@link ProductAPIResponse} holds product data and pricing data for the given product id.
     * @throws Exception
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Product Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @ApiOperation(value = "Retrieve Product and Price details by Product Id")
    @GetMapping(path = "/product/{productId}")
    ResponseEntity getProduct(@PathVariable("productId") int productId){

        logger.info("getProduct() Incoming request productId={}", productId);
        ProductAPIResponse productAPIResponse = productService.getProductDetail(productId);
        ResponseEntity responseEntity = null;
        if(productAPIResponse != null){
            responseEntity = new ResponseEntity<>(productAPIResponse, HttpStatus.OK);
        }else{
            logger.info("getProduct(), ProductDetail not found for id={}", productId);
            responseEntity = new ResponseEntity("Product not found for the Id",HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    /**
     * Put method to update the Product price data in datastore.
     * @param productId, Unique ID for the product
     * @param productUpdateRequest, product price update request
     * @return
     * @throws Exception
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Product Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @ApiOperation(value = "Retrieve Product and Price details by Product Id")
    @PutMapping(path = "/product/{productId}", consumes = "application/json")
    ResponseEntity updateProduct (@PathVariable("productId") int productId,
                                  @RequestBody ProductUpdateRequest productUpdateRequest) throws Exception{

        logger.info("updateProduct() Incoming update request = {}", productUpdateRequest.toString());
        ResponseEntity responseEntity = null;
        ProductAPIResponse productAPIResponse = null;

        if(validator.isValidRequest(productUpdateRequest, productId)) {
            productAPIResponse = productService.updateProductPriceData(productUpdateRequest);

            responseEntity = new ResponseEntity<>(productAPIResponse, HttpStatus.OK);
        }else{
            logger.error("updateProduct Endpoint, Error : Invalid request = {}", productUpdateRequest);
            throw new BadRequestException("Invalid ProductUpdate Request");
        }

        return responseEntity;
    }
}