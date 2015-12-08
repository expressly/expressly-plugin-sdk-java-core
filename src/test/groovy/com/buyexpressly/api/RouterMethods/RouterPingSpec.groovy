package com.buyexpressly.api.RouterMethods

import com.buyexpressly.api.MerchantServiceRoute

class RouterPingSpec extends RouterAbstractRouteSpec {

    @Override
    MerchantServiceRoute getRouteUnderTest() {
        MerchantServiceRoute.PING
    }

    def "The request is correctly parsed and the response is correctly formatted"() {
        when: "I invoke the route"
        router.route(request, response)

        then: "I can see that the response is written correctly"
        getResponseString().replaceAll('\\s*', '') == '{ "expressly": "Stuff is happening!" }'.replaceAll('\\s*', '')
    }
}
