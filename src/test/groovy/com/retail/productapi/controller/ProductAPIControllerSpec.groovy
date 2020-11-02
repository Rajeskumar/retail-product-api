package com.retail.productapi.controller

import com.retail.productapi.domain.ProductAPIResponse
import com.retail.productapi.domain.ProductPriceRequest
import com.retail.productapi.domain.ProductPriceResponse
import com.retail.productapi.domain.ProductUpdateRequest
import com.retail.productapi.service.ProductService
import com.retail.productapi.validator.ProductUpdateRequestValidator
import org.springframework.data.cassandra.CassandraConnectionFailureException
import org.springframework.http.HttpStatus
import spock.lang.Specification

class ProductAPIControllerSpec extends Specification {

    ProductAPIController productAPIController
    ProductService productService = Mock(ProductService)
    ProductUpdateRequestValidator validator
    ProductAPIResponse response
    ProductPriceResponse priceResponse
    ProductUpdateRequest updateRequest
    ProductPriceRequest priceRequest

    def setup(){
        productAPIController = new ProductAPIController()
        productAPIController.productService = productService
        productAPIController.validator = new ProductUpdateRequestValidator()
        response = new ProductAPIResponse()
    }


    def "GetProduct - Happy Path Scenario"() {

        given:
        int productId = 123456
        priceResponse = new ProductPriceResponse(120.99, "USD")
        response = new ProductAPIResponse(productId, "Apple Pencil", priceResponse)

        when:
        def actual = productAPIController.getProduct(productId)

        then:
        1 * productService.getProductDetail(productId) >> response

        actual.statusCode == HttpStatus.OK
        actual.hasBody()
        actual.body == response

    }

    def "GetProduct - Product not found"() {

        given:
        int productId = 123456
        response = null

        when:
        def actual = productAPIController.getProduct(productId)

        then:
        1 * productService.getProductDetail(productId) >> response

        actual.statusCode == HttpStatus.NOT_FOUND


    }

    def "UpdateProduct - Happy Path Scenario"() {

        given:
        int productId = 123456
        priceRequest = new ProductPriceRequest(120.99, "USD")
        updateRequest = new ProductUpdateRequest(productId, "Apple Pencil", priceRequest)
        response.productId = productId
        response.productPriceResponse= new ProductPriceResponse(priceRequest.value, priceRequest.currency_code)

        when:
        def actual = productAPIController.updateProduct(productId, updateRequest)

        then:
        1 * productService.updateProductPriceData(updateRequest) >> response

        actual.statusCode == HttpStatus.OK

        actual.hasBody()
        actual.body == response
    }

    def "UpdateProduct - Invalid update Request"() {

        given:
        int productId = 123456
        updateRequest = new ProductUpdateRequest(productId, "Apple Pencil", null)

        when:
        def actual = productAPIController.updateProduct(productId, updateRequest)

        then:
        0 * productService.updateProductPriceData(updateRequest) >> response

        actual.statusCode == HttpStatus.BAD_REQUEST

    }

    def "UpdateProduct - Datastore host not available"() {

        given:
        int productId = 123456
        priceRequest = new ProductPriceRequest(120.99, "USD")
        updateRequest = new ProductUpdateRequest(productId, "Apple Pencil", priceRequest)

        when:
        def actual = productAPIController.updateProduct(productId, updateRequest)

        then:
        1 * productService.updateProductPriceData(updateRequest) >> {throw new CassandraConnectionFailureException(new HashMap<InetSocketAddress, Throwable>(), "Host not available", new Throwable())}

        actual.statusCode == HttpStatus.INTERNAL_SERVER_ERROR

    }
}
