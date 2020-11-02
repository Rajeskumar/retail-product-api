package com.retail.productapi.integration


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class ProductApiIntegrationSpec extends Specification {

    @Autowired
    ApplicationContext context

    @Autowired
    MockMvc mvc

    def "test context loads"() {
        expect:
        context != null
        context.containsBean("productAPIController")
        context.containsBean("productServiceImpl")
        context.containsBean("productUpdateRequestValidator")
    }


    def "hello word test"(){

        when:
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/")).andReturn()
        def response = result.response.contentAsString

        then:
        response == "Welcome to ProductAPI"
    }

    def "Get Product API - Happy path scenario"(){

        setup:
        def expectedJson = '{"id":13264003,"name":"Jif Natural Creamy Peanut Butter - 40oz","current_price":{"value":13.99,"currency_code":"USD"}}'
        when:
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/product/13264003")).andReturn()

        then:
        result.response.getStatus() == 200
        result.response.contentAsString == expectedJson

    }

    def "Get Product API - Product not found in any source"(){

        when:
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/product/1234567")).andReturn()

        then:
        result.response.getStatus() == 404
        result.response.contentAsString == "Product not found for the Id"

    }

    def "Get Product API - Bad request"(){

        when:
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/product/adfs")).andReturn()

        then:
        result.response.getStatus() == 400

    }

    def "Update Product Price API - Happy path scenario"(){

        setup:
        def requestBody = '{"id":13264003,"name":"Jif Natural Creamy Peanut Butter - 40oz","current_price":{"value":13.99,"currency_code":"USD"}}'
        def expectedJson = '{"id":13264003,"name":null,"current_price":{"value":13.99,"currency_code":"USD"}}'
        when:
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/product/13264003")
                .content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn()

        then:
        result.response.getStatus() == 200
        result.response.contentAsString == expectedJson

    }

    def "Update Product Price API - Invalid request body"(){

        setup:
        def requestBody = '{"id":13264003,"name":"Jif Natural Creamy Peanut Butter - 40oz","current_price":{"value":, "currency_code":"USD"}}'
        when:
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/product/13264003")
                .content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn()

        then:
        result.response.getStatus() == 400
        result.response.contentAsString == ""

    }

    def "Update Product Price API - product id mismatch in request body"(){

        setup:
        def requestBody = '{"id":123456,"name":"Jif Natural Creamy Peanut Butter - 40oz","current_price":{"value":13.99, "currency_code":"USD"}}'
        when:
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/product/13264003")
                .content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn()

        then:
        result.response.getStatus() == 400
        result.response.contentAsString == "Invalid ProductUpdate Request"

    }

    def "Update Product Price API - product id not found in datastore"(){

        setup:
        def requestBody = '{"id":123456,"name":"Jif Natural Creamy Peanut Butter - 40oz","current_price":{"value":13.99, "currency_code":"USD"}}'
        def expectedResponse = '{"id":123456,"name":null,"current_price":{"value":13.99,"currency_code":"USD"}}'
        when:
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/product/123456")
                .content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn()

        then:
        result.response.getStatus() == 200
        result.response.contentAsString == expectedResponse

    }

    def "Update Product Price API - Additional json node in the update request"(){

        setup:
        def requestBody = '{"id":13264003,"name":"Jif Natural Creamy Peanut Butter - 40oz", "category":"Food", "current_price":{"value":13.99,"currency_code":"USD"}}'
        def expectedJson = '{"id":13264003,"name":null,"current_price":{"value":13.99,"currency_code":"USD"}}'
        when:
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/product/13264003")
                .content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn()

        then:
        result.response.getStatus() == 200
        result.response.contentAsString == expectedJson

    }
}
