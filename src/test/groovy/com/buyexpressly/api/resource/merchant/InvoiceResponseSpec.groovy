package com.buyexpressly.api.resource.merchant

import com.buyexpressly.api.resource.error.ExpresslyException
import com.buyexpressly.api.util.ObjectMapperFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.joda.time.LocalDate
import spock.lang.Specification

class InvoiceResponseSpec extends Specification {
    private ObjectMapper om

    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
        om = ObjectMapperFactory.make()
    }

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
        Map result = om.readValue(om.writeValueAsString(InvoiceResponse.builder()
                .withEmail("a@mail.com")
                .withPostTaxTotal(new BigDecimal(120))
                .withPreTaxTotal(new BigDecimal(100))
                .withTax(new BigDecimal(20))
                .add(generateOrder())
                .add(generateOrder())
                .build()
        ), Map)

        then:
        result.email == 'a@mail.com'
        result.orderCount == 2
        result.preTaxTotal == 100.00G
        result.postTaxTotal == 120.00G
        result.tax == 20.00
        result.orders.size() == 2
        result.orders.get(0).id == 'SDF-123'
        result.orders.get(0).date == '2015-12-14'
        result.orders.get(0).itemCount == 1
        result.orders.get(0).coupon == 'COUPON_CODE'
        result.orders.get(0).currency == 'GBP'
        result.orders.get(0).preTaxTotal == 50.00G
        result.orders.get(0).postTaxTotal == 60.00G
        result.orders.get(0).tax == 10.00G
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
