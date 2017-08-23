package com.buyexpressly.api.resource.merchant

import com.buyexpressly.api.resource.error.ExpresslyException
import com.buyexpressly.api.util.ObjectMapperFactory
import org.joda.time.LocalDate
import spock.lang.Specification

class InvoiceOrderResponseSpec extends Specification {
    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
    }

    def "a InvoiceOrderResponse object can be built"() {
        when: "I try to build an InvoiceOrderResponse object"
        InvoiceOrderResponse entity = generateOrder()

        then: "I can see that the values are populated correctly"
        entity instanceof InvoiceOrderResponse
        entity.postTaxTotal == new BigDecimal(60)
        entity.preTaxTotal == new BigDecimal(50)
        entity.tax == new BigDecimal(10)
        entity.coupon == "COUPON_CODE"
        entity.currency == "GBP"
        entity.itemCount == 1
        entity.orderDate == LocalDate.parse("2015-12-14")
        entity.orderId == "SDF-123"


    }

    def "an InvoiceOrderResponse object cannot be built if the post tax total is not correct"() {
        when: "I try to build a ping response"
        InvoiceOrderResponse entity = InvoiceOrderResponse.builder()
                .setPreTaxTotal(new BigDecimal(50))
                .setTax(new BigDecimal(10))
                .setPostTaxTotal(new BigDecimal(55))
                .setCoupon("COUPON_CODE")
                .setCurrency("GBP")
                .setItemCount(1)
                .setOrderDate(LocalDate.parse("2015-12-14"))
                .setOrderId("SDF-123")
                .build()

        then: "I can see that the values are populated correctly"
        entity == null
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "preTaxTotal + tax must equal postTaxTotal"
    }

    def "I can generate a json string from an invoice response object"() {
        when:
        def parsed = ObjectMapperFactory.make().writeValueAsString(generateOrder())

        then:
        parsed == """{"id":"SDF-123","date":"2015-12-14","itemCount":1,"coupon":"COUPON_CODE","currency":"GBP","preTaxTotal":50.00,"postTaxTotal":60.00,"tax":10.00}"""
    }

    InvoiceOrderResponse generateOrder() {
        return InvoiceOrderResponse.builder()
                .setPreTaxTotal(new BigDecimal(50))
                .setTax(new BigDecimal(10))
                .setPostTaxTotal(new BigDecimal(60))
                .setCoupon("COUPON_CODE")
                .setCurrency("GBP")
                .setItemCount(1)
                .setOrderDate(LocalDate.parse("2015-12-14"))
                .setOrderId("SDF-123")
                .build()
    }
}
