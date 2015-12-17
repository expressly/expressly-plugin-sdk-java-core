package com.buyexpressly.api.resource.merchant

import com.buyexpressly.api.resource.error.ExpresslyException
import org.codehaus.jackson.map.ObjectMapper
import org.joda.time.LocalDate
import spock.lang.Specification

class InvoiceResponseSpec extends Specification {
    def "a InvoiceResponse object can be built"() {
        when: "I try to build a ping response"
        InvoiceResponse entity = InvoiceResponse.builder()
                .withEmail("a@email.com")
                .withPostTaxTotal(new BigDecimal(120))
                .withPreTaxTotal(new BigDecimal(100))
                .withTax(new BigDecimal(20))
                .add(generateOrder())
                .add(generateOrder())
                .build()

        then: "I can see that the values are populated correctly"
        entity instanceof InvoiceResponse
        entity.email == "a@email.com"
        entity.postTaxTotal == new BigDecimal(120)
        entity.preTaxTotal == new BigDecimal(100)
        entity.tax == new BigDecimal(20)
        entity.orders.get(0).coupon == "COUPON_CODE"
        entity.orders.get(1).coupon == "COUPON_CODE"
        entity.orderCount == 2
    }

    def "an InvoiceResponse object cannot be built if the post tax total is not correct"() {
        when: "I try to build a ping response"
        InvoiceResponse entity = InvoiceResponse.builder()
                .withEmail("a@email.com")
                .withPostTaxTotal(new BigDecimal(140))
                .withPreTaxTotal(new BigDecimal(100))
                .withTax(new BigDecimal(20))
                .add(generateOrder())
                .add(generateOrder())
                .build()

        then: "I can see that the values are populated correctly"
        entity == null
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "invoice total [140.00] does not not match sum of orders' totals [120.00]"
    }

    def "I can generate a json string from an invoice response object"() {
        when:
        def parsed = new ObjectMapper().writeValueAsString(InvoiceResponse.builder()
                .withEmail("a@mail.com")
                .withPostTaxTotal(new BigDecimal(120))
                .withPreTaxTotal(new BigDecimal(100))
                .withTax(new BigDecimal(20))
                .add(generateOrder())
                .add(generateOrder())
                .build()
        )

        then:
        parsed == """{"email":"a@mail.com","orderCount":2,"preTaxTotal":100.00,"postTaxTotal":120.00,"tax":20.00,"orders":[{"id":"SDF-123","date":"2015-12-14","itemCount":1,"coupon":"COUPON_CODE","currency":"GBP","preTaxTotal":50.00,"postTaxTotal":60.00,"tax":10.00},{"id":"SDF-123","date":"2015-12-14","itemCount":1,"coupon":"COUPON_CODE","currency":"GBP","preTaxTotal":50.00,"postTaxTotal":60.00,"tax":10.00}]}"""
    }

    def InvoiceOrderResponse generateOrder() {
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
