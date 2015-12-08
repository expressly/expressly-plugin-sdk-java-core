package com.buyexpressly.api

import spock.lang.Specification
import spock.lang.Unroll

class MerchantServiceRouteSpec extends Specification {
    @Unroll("I can match routes correctly based on the regex and methods for #expectedRoute")
    def "I can match routes correctly based on the regex and methods"() {
        when: "I try to match the route"
        MerchantServiceRoute route = MerchantServiceRoute.findRoute(method, uri)

        then: "I see that I have matched the correct route"
        route == expectedRoute
        route.httpMethod == method
        route.getUriParameters(uri) == expectedParameters
        uri =~ route.uriRegex

        where:
        method | uri                                                           | expectedRoute                          | expectedParameters
        'GET'  | '/expressly/api/ping'                                         | MerchantServiceRoute.PING              | [:]
        'GET'  | '/expressly/api/registered'                                   | MerchantServiceRoute.PING_REGISTERED   | [:]
        'GET'  | '/expressly/api/2d5ed0ea-077b-44ab-870f-dd056b446fe7'         | MerchantServiceRoute.DISPLAY_POPUP     | [campaignCustomerUuid: '2d5ed0ea-077b-44ab-870f-dd056b446fe7']
        'GET'  | '/expressly/api/user/a@example.com'                           | MerchantServiceRoute.GET_CUSTOMER      | [email: 'a@example.com']
        'POST' | '/expressly/api/batch/invoice'                                | MerchantServiceRoute.GET_INVOICES      | [:]
        'POST' | '/expressly/api/batch/customer'                               | MerchantServiceRoute.CHECK_EMAILS      | [:]
        'GET' | '/expressly/api/2d5ed0ea-077b-44ab-870f-dd056b446fe7/migrate' | MerchantServiceRoute.CONFIRM_MIGRATION | [campaignCustomerUuid: '2d5ed0ea-077b-44ab-870f-dd056b446fe7']
    }

    def "If no route is matched then an exception is thrown"() {
        when: "I try to match the route"
        MerchantServiceRoute.findRoute(method, uri)

        then: "I expect an exception"
        IllegalArgumentException exception = thrown()
        exception.message == "invalid expressly merchant route, httpMethod=${method}, uri=${uri}"

        where:
        method | uri
        'POST' | '/expressly/api/ping'
        'GET'  | '/expressly/api/ping2'
    }
}
