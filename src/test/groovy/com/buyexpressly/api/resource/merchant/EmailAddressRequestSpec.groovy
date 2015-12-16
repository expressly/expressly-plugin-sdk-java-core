package com.buyexpressly.api.resource.merchant

import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class EmailAddressRequestSpec extends Specification {
    def "an EmailAddressRequest object can be parsed from json"() {
        given: "I have an email address request body to parse"
        def emailA = "a@email.com"
        def emailB = "b@email.com"
        def emailC = "c@email.com"
        String requestBody = """
            {
              "emails" : [
                  "$emailA",
                  "$emailB",
                  "$emailC"
                ]
            }
"""

        when: "I try to parse the string into an EmailAddressRequest object"
        EmailAddressRequest entity = new ObjectMapper().readValue(requestBody, EmailAddressRequest)

        then: "I can see that the values are populated correctly"
        entity instanceof EmailAddressRequest
        entity.emails == [emailA, emailB, emailC]

    }
}