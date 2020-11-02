package com.retail.productapi

import com.retail.productapi.domain.ProductAPIResponse
import com.retail.productapi.domain.ProductPriceResponse
import com.retail.productapi.service.ProductServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@SpringBootTest
@AutoConfigureMockMvc
class ProductApiIntegrationSpec extends Specification {

    @Autowired
    ApplicationContext context

    @Autowired
    MockMvc mvc

    @Autowired
    ProductServiceImpl productService

    def "test context loads"() {
        expect:
        context != null
        context.containsBean("productAPIController")
        context.containsBean("productServiceImpl")
        context.containsBean("productUpdateRequestValidator")
    }

    def "hello word test"(){

        given:
        productService.getProductDetail(123456) >> new ProductAPIResponse(123456,
                "Apple Pencil", new ProductPriceResponse(120.99, "USD"))

        when: "Calling root path of product-api"
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/product/123456")).andReturn()
        def response = result.response.contentAsString

        then:
        response == "Welcome"
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        ProductServiceImpl productService() {
            return detachedMockFactory.Stub(ProductServiceImpl)
        }
    }
}
