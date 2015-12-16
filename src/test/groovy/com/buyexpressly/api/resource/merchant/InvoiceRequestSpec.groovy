package com.buyexpressly.api.resource.merchant

import org.codehaus.jackson.map.ObjectMapper
import org.joda.time.LocalDate
import spock.lang.Specification

class InvoiceRequestSpec extends Specification {
    def "an InvoiceRequest object can be parsed from json"() {
        given: "I have an Invoice request body to parse"
        def expectedEmail = "test@email.com"
        def expectedFrom = LocalDate.now().minusWeeks(2).toString()
        def expectedTo = LocalDate.now().plusWeeks(2).toString()
        String requestBody = """
                  {
                    "email" : "$expectedEmail",
                    "from" : "$expectedFrom",
                    "to" : "$expectedTo"
                  }
        """

        when: "I try to parse the string into an InvoiceRequest object"
        InvoiceRequest entity = new ObjectMapper().readValue(requestBody, InvoiceRequest)

        then: "I can see that the values are populated correctly"
        entity instanceof InvoiceRequest
        entity.email == expectedEmail
        entity.from == LocalDate.parse(expectedFrom)
        entity.to == LocalDate.parse(expectedTo)
    }
}