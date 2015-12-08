package com.buyexpressly.api.RouterMethods

import com.buyexpressly.api.MerchantServiceRoute
import com.buyexpressly.api.resource.server.CartData
import com.buyexpressly.api.resource.server.CustomerData
import com.buyexpressly.api.resource.server.MigrationResponse
import org.codehaus.jackson.map.ObjectMapper

class RouterConfirmMigrationSpec extends RouterAbstractRouteSpec {

    @Override
    MerchantServiceRoute getRouteUnderTest() {
        MerchantServiceRoute.CONFIRM_MIGRATION
    }

    def "The request is correctly parsed and the response is correctly formatted"() {
        given: "I have a set campaignCustomerUuid"
        def expectedCampaignCustomerUuid = UUID.randomUUID().toString()
        String expectedEmail = "test@test.expressly.com"
        def redirectDestination = "redirect/destination"
        def expectedRoute = "/expressly/api/$expectedCampaignCustomerUuid/migrate"

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that the provider is called with the correct request data"
        2 * request.getRequestURI() >> expectedRoute

        1 * expresslyProvider.fetchMigrationCustomerData(_ as String) >> { String requestedCampaignCustomerUuid ->
            assert requestedCampaignCustomerUuid == expectedCampaignCustomerUuid
            def received = """
                {
                  "migration": {
                    "meta": {
                      "sender": "138"
                    },
                    "data": {
                      "email": "$expectedEmail",
                      "customerData": {
                        "firstName": "testName",
                        "lastName": "Burberry",
                        "gender": "M",
                        "billingAddress": 0,
                        "shippingAddress": 0,
                        "dateUpdated": "2015-11-24T16:19:16.000Z",
                        "numberOrdered": 0,
                        "emails": [
                          {
                            "email": "test@test.expressly.com"
                          }
                        ],
                        "phones": [
                          {
                            "type": "L",
                            "number": "020 7946 0975",
                            "countryCode": 44
                          }
                        ],
                        "addresses": [
                          {
                            "firstName": "testName",
                            "lastName": "Burberry",
                            "address1": "61 Wellfield Road",
                            "city": "Roath",
                            "zip": "CF24 3DG",
                            "phone": 0,
                            "stateProvince": "Cardiff",
                            "country": "GB"
                          }
                        ]
                      }
                    }
                  },
                  "cart": {
                    "couponCode": "test",
                    "productId": "123"
                  }
                }
            """

            ObjectMapper om = new ObjectMapper();
            om.readValue(received, MigrationResponse.class)

        }

        1 * provider.registerCustomer(_ as String, _ as CustomerData) >> {
            String requestEmail, CustomerData data ->
                assert requestEmail == expectedEmail
                assert data.emails.get(0).email == expectedEmail
                expectedEmail
        }
        1 * provider.checkCustomerIsNew(expectedEmail) >> true
        1 * provider.sendPasswordResetEmail(expectedEmail) >> true
        1 * provider.storeCartData(_ as String, _ as CartData) >> {
            String requestEmail, CartData data ->
                assert requestEmail == expectedEmail
                assert data.couponCode == "test"
                assert data.productId == "123"
                true
        }
        1 * expresslyProvider.finaliseMigrationOfCustomerData(expectedCampaignCustomerUuid) >> true
        1 * provider.loginCustomer(expectedEmail) >> redirectDestination

        and: "I can see that the response redirects the user correctly"
        1 * response.sendRedirect(redirectDestination);
    }
}
