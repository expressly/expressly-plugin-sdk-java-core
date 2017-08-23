package com.buyexpressly.api.resource.server

import com.buyexpressly.api.resource.error.ExpresslyException
import com.buyexpressly.api.util.ObjectMapperFactory
import spock.lang.Specification

class AddressSpec extends Specification {
    private String expectedAddress = '{' +
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

    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
    }

    def "an address can be mapped from a json string"() {
        given: "I have an address in its json representation"
        String json = expectedAddress

        when: "I map it"
        Address entity = ObjectMapperFactory.make().readValue(json, Address)

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

    def "I can create an address object using the builder"() {
        when: ""
        def entity = Address.builder()
                .withFirstName("Marc")
                .withLastName("Smith")
                .withAddress1("261 Wellfield Road")
                .withAddress2("Little Ham")
                .withCity("Roath")
                .withCompanyName("Expressly")
                .withZip("CF24 3DG")
                .withPhone("1")
                .withAddressAlias("Home")
                .withProvince("Cardiff")
                .withCountry("GB")
                .build()

        then: "I can see that the values are populated correctly"
        entity instanceof Address
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

    def "I can build an address object with just an address, city, zip, and country "() {
        when: "I try to build an address with just an address, city, zip and country fields"
        def entity = Address.builder()
                .withAddress1("261 Wellfield Road")
                .withCity("Roath")
                .withZip("CF24 3DG")
                .withCountry("GB")
                .build()

        then: "I can see that the values are populated correctly"
        entity instanceof Address
        with(entity) {
            address1 == '261 Wellfield Road'
            city == 'Roath'
            zip == 'CF24 3DG'
            country == 'GB'
        }
    }

    def "I can't build an address object without an address"() {
        when: "I try to build an address without an address field"
        Address.builder()
           //     .withAddress1("261 Wellfield Road")
                .withCity("Roath")
                .withZip("CF24 3DG")
                .withCountry("GB")
                .build()

        then: "I can see an exception is thrown"
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [address1] is required"
    }
   def "I can't build an address object without a city"() {
        when: "I try to build an address without an address field"
        Address.builder()
                .withAddress1("261 Wellfield Road")
                .withZip("CF24 3DG")
                .withCountry("GB")
                .build()

        then: "I can see an exception is thrown"
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [city] is required"
    }
   def "I can't build an address object without a zip"() {
        when: "I try to build an address without an address field"
        Address.builder()
                .withAddress1("261 Wellfield Road")
                .withCity("Roath")
                .withCountry("GB")
                .build()

        then: "I can see an exception is thrown"
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [zip] is required"
    }
   def "I can't build an address object without a country"() {
        when: "I try to build an address without an address field"
        Address.builder()
                .withAddress1("261 Wellfield Road")
                .withCity("Roath")
                .withZip("CF24 3DG")
                .build()

        then: "I can see an exception is thrown"
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [country] is required"
    }
}
