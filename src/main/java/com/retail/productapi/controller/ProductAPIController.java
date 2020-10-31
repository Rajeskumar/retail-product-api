package com.retail.productapi.controller;

import com.retail.productapi.domain.ProductAPIResponse;
import com.retail.productapi.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value = "/api/v1", description = "Retail Product API end point which aggregates product details from multiple sources.")
public class ProductAPIController {

    @Autowired
    ProductService productService;

    @GetMapping(path = "/")
    @ResponseBody
    String welcome(){
        return "Welcome to ProductAPI";
    }


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Product Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @ApiOperation(value = "Retrieve Product and Price details by Product Id",
            notes = "")
    @GetMapping(path = "/product/{productId}")
    ResponseEntity<ProductAPIResponse> getProduct(@PathVariable("productId") int productId){

        ProductAPIResponse productAPIResponse = productService.getProductDetail(productId);
        ResponseEntity<ProductAPIResponse> responseEntity = null;
        if(productAPIResponse != null){
            responseEntity = new ResponseEntity<>(productAPIResponse, HttpStatus.OK);
        }else{
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

}