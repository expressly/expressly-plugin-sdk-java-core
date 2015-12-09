package com.buyexpressly.api.resource.server

import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class AddressSpec extends Specification {
    def "an address can be mapped from a json string"() {
        given: "I have an address in its json representation"
        String json = '{' +
                '"firstName":"Marc",' +
                '"lastName":"Smith",' +
                '"address1":"261 Wellfield Road",' +
                '"address2":"Little Ham",' +
                '"city":"Roath",' +
                '"zip":"CF24 3DG",' +
                '"companyName":"Expressly",' +
                '"phone":1,' +
                '"addressAlias":"Home",' +
                '"stateProvince":"Cardiff",' +
                '"country":"GB"}'

        when: "I map it"
        Address entity = new ObjectMapper().readValue(json, Address)

        then: "I can see that the values are populated correctly"
        with(entity) {
            firstName == 'Marc'
            lastName == 'Smith'
            address1 == '261 Wellfield Road'
            address2 == 'Little Ham'
            city == 'Roath'
            companyName == 'Expressly'
            zip == 'CF24 3DG'
            phone == 1
            addressAlias == 'Home'
            stateProvince == 'Cardiff'
            country == 'GB'
        }
    }
}
