package com.buyexpressly.api.RouterMethods

import com.buyexpressly.api.MerchantServiceRoute

class RouterPingRegisteredSpec extends RouterAbstractRouteSpec {

    @Override
    MerchantServiceRoute getRouteUnderTest() {
        MerchantServiceRoute.PING_REGISTERED
    }

    def "The request is correctly parsed and the response is correctly formatted"() {
        when: "I invoke the route"
        router.route(request, response)

        then: "I can see that the response is written correctly"
        Map result = objectMapper.readValue(responseString, Map)
        result.registered
        result.version == 'V2'
        result.lightbox == 'javascript'
        result.platformName == 'Expressly Java SDK'
        result.platformVersion == '2.7.0'
    }
}
