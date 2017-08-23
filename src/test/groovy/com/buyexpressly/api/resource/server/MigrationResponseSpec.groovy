package com.buyexpressly.api.resource.server

import com.buyexpressly.api.util.ObjectMapperFactory
import spock.lang.Specification

class MigrationResponseSpec extends Specification {

    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
    }

    def "I can map a full migration response object"() {
        given: "I have a properly formatted string that represents a json version of a migration response object"
        def expectedEmail = "test@email.com"
        String expectedMigrationResponse = expectedCustomerResponse(expectedEmail)

        when: "I try to map a full migration response object"
        MigrationResponse mapped = ObjectMapperFactory.make().readValue(expectedMigrationResponse, MigrationResponse)

        then: "I can see i can retrieve the information from the migration response"
        mapped instanceof MigrationResponse
        mapped.migrationData instanceof MigrationData
        mapped.migrationData.data.email == expectedEmail
        mapped.cartData instanceof CartData
    }

    String expectedCustomerResponse(String expectedEmail) {
        return """
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
    }

}
