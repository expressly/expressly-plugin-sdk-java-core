package com.buyexpressly.api.resource.merchant

import com.buyexpressly.api.util.ObjectMapperFactory
import org.joda.time.LocalDate
import spock.lang.Specification

class InvoiceListRequestSpec extends Specification {

    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
    }

    def "an InvoiceListRequest object can be parsed from json"() {
        given: "I have an Invoice request body to parse"
        def expectedEmail = "test@email.com"
        def expectedFrom = LocalDate.now().minusWeeks(2).toString()
        def expectedTo = LocalDate.now().plusWeeks(2).toString()
        String requestBody = """
            {
              "customers" : [
                  {
                    "email" : "$expectedEmail",
                    "from" : "$expectedFrom",
                    "to" : "$expectedTo"
                  }
                ]
            }
        """

        when: "I try to parse the string into an InvoiceListRequest object"
        InvoiceListRequest entity = ObjectMapperFactory.make().readValue(requestBody, InvoiceListRequest)

        then: "I can see that the values are populated correctly"
        entity instanceof InvoiceListRequest
        entity.customers.get(0).email == expectedEmail
        entity.customers.get(0).from == LocalDate.parse(expectedFrom)
        entity.customers.get(0).to == LocalDate.parse(expectedTo)
    }
}