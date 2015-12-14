package com.buyexpressly.api.RouterMethods

import com.buyexpressly.api.MerchantServiceRoute
import com.buyexpressly.api.resource.server.CustomerData
import org.codehaus.jackson.map.ObjectMapper

class RouterGetCustomerSpec extends RouterAbstractRouteSpec {

    @Override
    MerchantServiceRoute getRouteUnderTest() {
        MerchantServiceRoute.GET_CUSTOMER
    }

    def "The request is correctly parsed and the response is correctly formatted"() {
        given: "I have a set campaignCustomerUuid"
        String expectedEmail = "test@test.expressly.com"
        def expectedRoute = "/expressly/api/user/$expectedEmail"
        def customerData = generateCustomer(expectedEmail)

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that the provider is called with the correct request data"
        2 * request.getRequestURI() >> expectedRoute
        1 * provider.getCustomerData(_ as String) >> {
            String requestEmail ->
                requestEmail == expectedEmail
                ObjectMapper om = new ObjectMapper()
                om.readValue(customerData, CustomerData.class)
        }

        and: "I can see that the response is written correctly"
        responseString.replaceAll('\\s*', '') == expectedCustomerResponse(expectedEmail).replaceAll('\\s*', '')
    }

    def String expectedCustomerResponse(String expectedEmail) {
        return """
                    {"meta":{"sender":null,"locale":null,"issuerData":[]},"data":{"customerData":{"firstName":"testName","lastName":"Burberry","gender":"M","company":null,"dob":null,"taxNumber":null,"dateUpdated":"2015-11-24T16:19:16Z","dateLastOrder":null,"numberOrdered":0,"billingAddress":0,"shippingAddress":0,"onlinePresence":[],"emails":[{"alias":null,"email":"$expectedEmail"}],"phones":[{"type":"L","number":"02079460975","countryCode":44}],"addresses":[{"firstName":"testName","lastName":"Burberry","address1":"61WellfieldRoad","address2":null,"city":"Roath","companyName":null,"zip":"CF243DG","phone":0,"addressAlias":null,"stateProvince":"Cardiff","country":"GB"}]},"email":"$expectedEmail"}}
               """
    }

    def String generateCustomer(String expectedEmail) {
        """
           {"firstName":"testName","lastName":"Burberry","gender":"M","company":null,"dob":null,"taxNumber":null,"dateUpdated":"2015-11-24T16:19:16.000Z","dateLastOrder":null,"numberOrdered":0,"billingAddress":0,"shippingAddress":0,"onlinePresence":[],"emails":[{"alias":null,"email":"$expectedEmail"}],"phones":[{"type":"L","number":"02079460975","countryCode":44}],"addresses":[{"firstName":"testName","lastName":"Burberry","address1":"61WellfieldRoad","address2":null,"city":"Roath","companyName":null,"zip":"CF243DG","phone":0,"addressAlias":null,"stateProvince":"Cardiff","country":"GB"}]}
        """
    }
}
