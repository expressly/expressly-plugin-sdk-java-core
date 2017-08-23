package com.buyexpressly.api.resource.server

import com.buyexpressly.api.resource.error.ExpresslyException
import com.buyexpressly.api.util.ObjectMapperFactory
import spock.lang.Specification

class PhoneSpec extends Specification {
    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
    }

    def "a phone can be mapped from a json string"() {
        given: "I have a phone number in its json representation"
        String json = '{"type":"L","number":"555 123","countryCode":44}'

        when: "I map it"
        Phone phone = ObjectMapperFactory.make().readValue(json, Phone)

        then: "I can see that the values are populated correctly"
        phone.countryCode == 44
        phone.number == '555 123'
        phone.type == 'L'
    }

    def "a phone object can be built" () {
        when: "I try to build a phone object"
        Phone phone = Phone.builder()
                .withCountryCode(44)
                .withNumber("555 123")
                .withType("L")
                .build()

        then: "I can see that the values are populated correctly"
        phone.countryCode == 44
        phone.number == '555 123'
        phone.type == 'L'
    }
    def "a phone object can be built just with a phone number" () {
        when: "I try to build a phone object"
        Phone phone = Phone.builder()
                .withNumber("555 123")
                .build()

        then: "I can see that the values are populated correctly"
        phone.countryCode == null
        phone.number == '555 123'
        phone.type == null
    }

    def "I can't build a phone object without a phone number"(){
        when: "I try to build a phone object"
        Phone.builder()
                .withCountryCode(44)
                .withType("L")
                .build()

        then:"I can see an error occurs"
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [number] is required"
    }
}
