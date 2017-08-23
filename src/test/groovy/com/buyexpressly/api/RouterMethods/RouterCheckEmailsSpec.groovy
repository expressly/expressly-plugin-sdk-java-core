package com.buyexpressly.api.RouterMethods

import com.buyexpressly.api.MerchantServiceRoute
import com.buyexpressly.api.resource.merchant.EmailAddressRequest
import com.buyexpressly.api.resource.merchant.EmailStatusListResponse

class RouterCheckEmailsSpec extends RouterAbstractRouteSpec {

    @Override
    MerchantServiceRoute getRouteUnderTest() {
        MerchantServiceRoute.CHECK_EMAILS
    }

    def "The request is correctly parsed and the response is correctly formatted"() {
        given: "I have a valid request route"
        String emailA = "a@test.com"
        String emailB = "b@test.com"
        String emailC = "c@test.com"
        String emailD = "d@test.com"
        1 * request.getReader() >> new BufferedReader(new StringReader(
                """
                    {
                        "emails": [
                            "$emailA",
                            "$emailB",
                            "$emailC",
                            "$emailD"
                        ]
                    }
            """))

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that the provider is called with the correct request data"
        1 * provider.checkCustomerStatus(_ as EmailAddressRequest) >> { EmailAddressRequest emails ->
            assert emails.emails.size() == 4
            assert  emails.emails == ["a@test.com", "b@test.com", "c@test.com", "d@test.com"]
            EmailStatusListResponse.builder().addExisting(emails.emails.subList(0, 1)).build()
        }

        and: "I can see that the response is written correctly"
        responseString.replaceAll('\\s*', '') == """
                {
                    "existing": ["a@test.com"]

                }
            """.replaceAll('\\s*', '')
    }
}
