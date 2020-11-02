package com.retail.productapi.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.retail.productapi.dao.ProductPriceRepository
import com.retail.productapi.domain.*
import org.springframework.data.cassandra.CassandraConnectionFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpServerErrorException
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class ProductServiceImplSpec extends Specification {

    ProductServiceImpl productService

    ObjectMapper objectMapper = Mock(ObjectMapper)
    ProductPriceRepository priceRepository = Mock(ProductPriceRepository)
    ExternalAPIService<RedskyAPIProductData> externalAPIService = Mock(ExternalAPIService)
    Product product
    ProductItem item
    ProductDescription description
    RedskyAPIProductData redskyAPIProductData
    ProductUpdateRequest productUpdateRequest

    def setup(){
        product = new Product()
        item = new ProductItem()
        description = new ProductDescription()
        redskyAPIProductData = new RedskyAPIProductData()
        productService = new ProductServiceImpl(priceRepository, objectMapper, externalAPIService)
    }

    def "GetProductDetail - HappyPath scenario"() {

        given:
        int productId = 123456
        ProductPriceData priceData = new ProductPriceData(123456, '{"value":104.99, "currency_code":"USD"}')

        description.title="Apple Pencil"
        item.productDescription=description
        product.productItem=item
        redskyAPIProductData.product=product

        when:
        def actual = productService.getProductDetail(productId)

        then:
        1 * priceRepository.findById(productId) >> new Optional<ProductPriceData>(priceData)
        1 * externalAPIService.fetchAPIResponse(_,_) >> new CompletableFuture<ResponseEntity<RedskyAPIProductData>>()
                .completedFuture(new ResponseEntity<RedskyAPIProductData>(redskyAPIProductData, HttpStatus.OK))
        1 * objectMapper.readValue(_,_) >> new ProductPriceResponse(104.99, '"currency_code":"USD"')

        actual.productId == 123456
        actual.productName == "Apple Pencil"
        actual.productPriceResponse.value == 104.99
        actual.productPriceResponse.currency_code == '"currency_code":"USD"'
    }

    def "GetProductDetail - No PriceData in datastore"() {
        given:
        int productId = 123456

        description.title="Apple Pencil"
        item.productDescription=description
        product.productItem=item
        redskyAPIProductData.product=product

        when:
        def actual = productService.getProductDetail(productId)

        then:
        1 * priceRepository.findById(productId) >> new Optional<ProductPriceData>()
        0 * objectMapper.readValue(_,_)
        1 * externalAPIService.fetchAPIResponse(_,_) >> new CompletableFuture<ResponseEntity<RedskyAPIProductData>>()
                .completedFuture(new ResponseEntity<RedskyAPIProductData>(redskyAPIProductData, HttpStatus.OK))

        actual.productId == 123456
        actual.productName == "Apple Pencil"
        actual.productPriceResponse == null
    }

    def "GetProductDetail - No RedskyAPI data"(){

        given:
        int productId = 123456
        ProductPriceData priceData = new ProductPriceData(123456, '{"value":104.99, "currency_code":"USD"}')

        redskyAPIProductData.product=product

        when:
        def actual = productService.getProductDetail(productId)

        then:
        1 * priceRepository.findById(productId) >> new Optional<ProductPriceData>(priceData)
        1 * objectMapper.readValue(_,_) >> new ProductPriceResponse(104.99, '"currency_code":"USD"')
        1 * externalAPIService.fetchAPIResponse(_,_) >> new CompletableFuture<ResponseEntity<RedskyAPIProductData>>()
                .completedFuture(new ResponseEntity<RedskyAPIProductData>(redskyAPIProductData, HttpStatus.OK))

        actual.productId == 123456
        actual.productName == ""
        actual.productPriceResponse.value == 104.99
        actual.productPriceResponse.currency_code == '"currency_code":"USD"'
    }

    def "GetProductDetail - datastore not available"() {
        given:
        int productId = 123456

        description.title="Apple Pencil"
        item.productDescription=description
        product.productItem=item
        redskyAPIProductData.product=product

        when:
        def actual = productService.getProductDetail(productId)

        then:
        1 * priceRepository.findById(productId) >> {throw new CassandraConnectionFailureException(new HashMap<InetSocketAddress, Throwable>(), "Host not available", new Throwable())}
        0 * objectMapper.readValue(_,_)
        1 * externalAPIService.fetchAPIResponse(_,_) >> new CompletableFuture<ResponseEntity<RedskyAPIProductData>>()
                .completedFuture(new ResponseEntity<RedskyAPIProductData>(redskyAPIProductData, HttpStatus.OK))

        actual.productId == 123456
        actual.productName == "Apple Pencil"
        actual.productPriceResponse == null
    }

    def "GetProductDetail - Redsky API Server error"() {
        given:
        int productId = 123456
        ProductPriceData priceData = new ProductPriceData(123456, '{"value":104.99, "currency_code":"USD"}')

        when:
        def actual = productService.getProductDetail(productId)

        then:
        1 * priceRepository.findById(productId) >> new Optional<ProductPriceData>(priceData)
        1 * objectMapper.readValue(_,_) >> new ProductPriceResponse(104.99, '"currency_code":"USD"')
        1 * externalAPIService.fetchAPIResponse(_,_) >> new CompletableFuture<ResponseEntity<RedskyAPIProductData>>()
                .completedFuture(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))

        actual.productId == 123456
        actual.productName == null
        actual.productPriceResponse.value == 104.99
        actual.productPriceResponse.currency_code == '"currency_code":"USD"'
    }

    def "GetProductPriceData - HappyPath Scenario"() {

        given:
        int productId = 13860428
        ProductPriceData priceData = new ProductPriceData(13860428, '{"value":104.99, "currency_code":"USD"}')

        when:
        def actual = productService.getProductPriceData(productId)

        then:
        1 * priceRepository.findById(productId) >> new Optional<ProductPriceData>(priceData)
        actual.productId == 13860428
        actual.productPrice == '{"value":104.99, "currency_code":"USD"}'

    }

    def "GetProductPriceData - no product pricedata" () {

        given:
        int productId = 13860428

        when:
        def actual = productService.getProductPriceData(productId)

        then:
        1 * priceRepository.findById(productId) >> new Optional<ProductPriceData>()
        actual == null
    }

    def "GetProductPriceData - datastore host unavailable" () {

        given:
        int productId = 13860428

        when:
        def actual = productService.getProductPriceData(productId)

        then:
        1 * priceRepository.findById(productId) >> {throw new CassandraConnectionFailureException(new HashMap<InetSocketAddress, Throwable>(), "Host not available", new Throwable())}
        actual == null
    }

    def "updateProductPriceData() - HappyPath scenario"(){

        given:
        productUpdateRequest = new ProductUpdateRequest(123456, "Apple Pencil", new ProductPriceRequest(120.99, "USD"))
        String priceData = '{"value":120.99, "currency_code":"USD"}'

        when:
        def actual = productService.updateProductPriceData(productUpdateRequest)

        then:
        1 * objectMapper.writeValueAsString(_) >> priceData
        1 * priceRepository.save(_) >> new ProductPriceData(123456, priceData)
        1 * objectMapper.readValue(_,_) >> new ProductPriceResponse(120.99, "USD")

        actual != null
        actual.productId == 123456
        actual.productPriceResponse.value == 120.99
        actual.productPriceResponse.currency_code == "USD"

    }

    def "updateProductPriceData() - Invalid Price data in the request"(){

        given:
        productUpdateRequest = new ProductUpdateRequest(123456, "Apple Pencil", new ProductPriceRequest(120.99, "USD"))

        when:
        def actual = productService.updateProductPriceData(productUpdateRequest)

        then:
        1 * objectMapper.writeValueAsString(_) >> {throw new JsonProcessingException("Parser error")}
        0 * priceRepository.save(_)
        0 * objectMapper.readValue(_,_)

        thrown(Exception)
    }

}
