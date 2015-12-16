package com.buyexpressly.api.resource.server

import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class CartDataSpec extends Specification {
    def "a CartData object can be parsed from a received json string"() {
        given: "I have a CartData in its properly formatted json representation"
        def expectedCouponCode = 'abc'
        def expectedProductId = '123'

        String json = '{' +
                '"couponCode" : "' + expectedCouponCode + '",' +
                '"productId" : "' + expectedProductId + '"' +
                '}'

        when: "I map it"
        CartData entity = new ObjectMapper().readValue(json, CartData)

        then: "I can see that the values are populated correctly"
        entity.couponCode == expectedCouponCode
        entity.productId == expectedProductId
    }
}
