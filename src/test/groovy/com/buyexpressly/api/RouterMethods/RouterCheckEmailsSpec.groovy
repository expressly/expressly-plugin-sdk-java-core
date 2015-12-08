package com.buyexpressly.api.RouterMethods

import com.buyexpressly.api.MerchantServiceRoute

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
        1 * provider.getExistingEmails(_ as List<String>) >> { List<String> emails ->
            assert emails.get(0).size() == 4
            assert  emails.get(0) == ["a@test.com", "b@test.com", "c@test.com", "d@test.com"]
            [emails.get(0).get(1)]
        }
        1 * provider.getPendingEmails(_ as List<String>) >> { List<String> emails ->
            assert emails.get(0).size() == 4
            assert  emails.get(0) == ["a@test.com", "b@test.com", "c@test.com", "d@test.com"]
            [emails.get(0).get(2)]
        }
        1 * provider.getDeletedEmails(_ as List<String>) >> { List<String> emails ->
            assert emails.get(0).size() == 4
            assert  emails.get(0) == ["a@test.com", "b@test.com", "c@test.com", "d@test.com"]
            [emails.get(0).get(0)]
        }

        and: "I can see that the response is written correctly"
        responseString.replaceAll('\\s*', '') == """
                {
                    "existing": ["b@test.com"],
                    "pending" : ["c@test.com"],
                    "deleted" : ["a@test.com"]

                }
            """.replaceAll('\\s*', '')
    }
}
