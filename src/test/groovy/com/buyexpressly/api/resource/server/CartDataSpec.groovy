package com.buyexpressly.api.resource.server

import com.buyexpressly.api.util.ObjectMapperFactory
import spock.lang.Specification

class CartDataSpec extends Specification {
    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
    }

    def "a CartData object can be parsed from a received json string"() {
        given: "I have a CartData in its properly formatted json representation"
        def expectedCouponCode = 'abc'
        def expectedProductId = '123'

        String json = '{' +
                '"couponCode" : "' + expectedCouponCode + '",' +
                '"productId" : "' + expectedProductId + '"' +
                '}'

        when: "I map it"
        CartData entity = ObjectMapperFactory.make().readValue(json, CartData)

        then: "I can see that the values are populated correctly"
        entity.couponCode == expectedCouponCode
        entity.productId == expectedProductId
    }
}
