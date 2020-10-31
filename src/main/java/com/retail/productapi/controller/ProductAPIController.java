package com.retail.productapi.controller;

import com.retail.productapi.domain.ProductAPIResponse;
import com.retail.productapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductAPIController {

    @Autowired
    ProductService productService;

    @GetMapping(path = "/")
    @ResponseBody
    String welcome(){
        return "Welcome to ProductAPI";
    }


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