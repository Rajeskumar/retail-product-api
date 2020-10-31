package com.retail.productapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.retail.productapi.dao.ProductPriceRepository
import com.retail.productapi.domain.Product
import com.retail.productapi.domain.ProductDescription
import com.retail.productapi.domain.ProductItem
import com.retail.productapi.domain.ProductPriceData
import com.retail.productapi.domain.ProductPriceResponse
import com.retail.productapi.domain.RedskyAPIProductData
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class ProductServiceImplSpec extends Specification {

    ProductServiceImpl productService

    ObjectMapper objectMapper = Mock(ObjectMapper)
    ProductPriceRepository priceRepository = Mock(ProductPriceRepository)
    ExternalAPIService<RedskyAPIProductData> externalAPIService = Mock(ExternalAPIService)
    Product product
    ProductItem item
    ProductDescription description
    RedskyAPIProductData redskyAPIProductData

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
        priceRepository.findById(productId) >> new Optional<ProductPriceData>(priceData)

        description.title="Apple Pencil"
        item.productDescription=description
        product.productItem=item
        redskyAPIProductData.product=product
        externalAPIService.fetchAPIResponse(_,_) >> new ResponseEntity<>(redskyAPIProductData, HttpStatus.OK)

        objectMapper.readValue(_,_) >> new ProductPriceResponse(104.99, '"currency_code":"USD"')

        when:
        def actual = productService.getProductDetail(productId)

        then:
        actual.productId == 123456
        actual.productName == "Apple Pencil"
        actual.productPriceResponse.value == 104.99
        actual.productPriceResponse.currency_code == '"currency_code":"USD"'
    }

    def "GetProductDetail - No PriceData in datastore"() {

    }

    def "GetProductDetail - No RedskyAPI data"(){

    }

    def "GetProductPriceData - HappyPath Scenario"() {

        given:
        int productId = 13860428
        ProductPriceData priceData = new ProductPriceData(13860428, '{"value":104.99, "currency_code":"USD"}')
        priceRepository.findById(productId) >> new Optional<ProductPriceData>(priceData)

        when:
        def actual = productService.getProductPriceData(productId)

        then:
        actual.productId == 13860428
        actual.productPrice == '{"value":104.99, "currency_code":"USD"}'

    }

    def "GetProductPriceData - no product pricedata" () {

        given:
        int productId = 13860428
        priceRepository.findById(productId) >> new Optional<ProductPriceData>()

        when:
        def actual = productService.getProductPriceData(productId)

        then:
        actual == null
    }

    def "GetProductPriceData - datastore unavailable"(){

    }

    def "GetRedskyAPIProductData - HappyPath scenario"() {

    }

    def "GetRedskyAPIProductData - No product data"(){

    }

    def "GetRedskyAPIProductData - redsky api timeout "(){

    }

}
