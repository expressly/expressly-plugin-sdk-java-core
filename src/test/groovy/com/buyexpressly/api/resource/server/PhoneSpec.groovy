package com.buyexpressly.api.resource.server

import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class PhoneSpec extends Specification {
    def "a phone can be mapped from a json string"() {
        given: "I have a phone number in its json representation"
        String json = '{"type":"L","number":"555 123","countryCode":44}'

        when: "I map it"
        Phone phone = new ObjectMapper().readValue(json, Phone)

        then: "I can see that the values are populated correctly"
        phone.countryCode == 44
        phone.number == '555 123'
        phone.type == 'L'
    }
}
