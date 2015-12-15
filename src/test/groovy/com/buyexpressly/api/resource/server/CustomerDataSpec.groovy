package com.buyexpressly.api.resource.server

import org.codehaus.jackson.map.ObjectMapper
import org.joda.time.DateTime
import org.joda.time.LocalDate
import spock.lang.Specification

class CustomerDataSpec extends Specification {
    def "a customer can be mapped from a json string"() {
        given: "I have a customer in its json representation"
        String json = '{' +
                '"firstName":"Marc",' +
                '"lastName":"Smith",' +
                '"gender":"M",' +
                '"billingAddress":0,' +
                '"shippingAddress":0,' +
                '"dateUpdated":"2015-11-25T13:21:04.000Z",' +
                '"numberOrdered":0,' +
                '"onlinePresence":[{"field":"twitter","value":"mgsmith57"}],' +
                '"emails":[{"email":"someone@test.com"}],' +
                '"phones":[{"type":"L","number":"555 123","countryCode":44}],' +
                '"addresses":[{"firstName":"Marc","lastName":"SMith","address1":"261 Wellfield Road","city":"Roath","zip":"CF24 3DG","phone":0,"stateProvince":"Cardiff","country":"GB"}]}'


        when: "I map it"
        CustomerData entity = new ObjectMapper().readValue(json, CustomerData)

        then: "I can see that the values are populated correctly"
        with(entity) {
            firstName == 'Marc'
            lastName == 'Smith'
            gender == 'M'
            billingAddress == 0
            shippingAddress == 0
            dateUpdated == DateTime.parse('2015-11-25T13:21:04+00:00')
            onlinePresence[0].field == 'twitter'
            onlinePresence[0].value == 'mgsmith57'
            emails[0].email == 'someone@test.com'
            phones[0].number == '555 123'
            phones[0].type == 'L'
            phones[0].countryCode == 44
            addresses[0].firstName == 'Marc'
            addresses[0].lastName == 'SMith'
            addresses[0].address1 == '261 Wellfield Road'
            addresses[0].city == 'Roath'
            addresses[0].zip == 'CF24 3DG'
            addresses[0].phone == 0
            addresses[0].stateProvince == 'Cardiff'
            addresses[0].country == 'GB'
        }
    }

    def "I can build a customer"() {
        given: "I have an object mapper to create the entities that don't have builders yet"
        ObjectMapper mapper = new ObjectMapper()

        when: "I use the builder"
        CustomerData entity = CustomerData.builder()
            .withBillingAddress(1)
            .withCompany("expressly")
            .withDob(LocalDate.parse('1979-03-12'))
            .withFirstName('Marc')
            .withGender('M')
            .withLastName('Smith')
            .withLastOrderTimeAtSource(LocalDate.parse('2015-10-19'))
            .withLastUpdatedAtSource(DateTime.parse('2015-11-25T13:21:04+00:00'))
            .withOrderItemCount(10)
            .withShippingAddress(2)
            .withTaxNumber("tax")
            .addAddress(mapper.readValue('{"address1":"addr1"}', Address))
            .addAddress(mapper.readValue('{"address1":"addr2"}', Address))
            .addEmailAddress(mapper.readValue('{"email":"one@test.com"}', Email))
            .addEmailAddress(mapper.readValue('{"email":"two@test.com"}', Email))
            .addOnlineIdentity(mapper.readValue('{"field":"twitter","value":"expressly"}', Tuple))
            .addOnlineIdentity(mapper.readValue('{"field":"facebook","value":"expressly"}', Tuple))
            .addPhoneNumber(mapper.readValue('{"type":"L","number":"555 123"}', Phone))
            .addPhoneNumber(mapper.readValue('{"type":"M","number":"555 678"}', Phone))
            .build()

        then: "I see that the values are set correctly"
        with(entity) {
            firstName == 'Marc'
            lastName == 'Smith'
            gender == 'M'
            company == 'expressly'
            billingAddress == 1
            shippingAddress == 2
            dob == LocalDate.parse('1979-03-12')
            dateLastOrder == LocalDate.parse('2015-10-19')
            dateUpdated == DateTime.parse('2015-11-25T13:21:04+00:00')
            numberOrdered == 10
            taxNumber == 'tax'
            onlinePresence[0].field == 'twitter'
            onlinePresence[1].field == 'facebook'
            emails[0].email == 'one@test.com'
            emails[1].email == 'two@test.com'
            phones[0].number == '555 123'
            phones[1].number == '555 678'
            addresses[0].address1 == 'addr1'
            addresses[1].address1 == 'addr2'
        }
    }
    def "I can build a customer with only a first, last name, gender and one address "() {
        given: "I have an object mapper to create the entities that don't have builders yet"
        ObjectMapper mapper = new ObjectMapper()

        when: "I use the builder"
        CustomerData entity = CustomerData.builder()
                .withFirstName('Marc')
                .withGender('M')
                .withLastName('Smith')
                .addAddress(mapper.readValue('{"address1":"addr1"}', Address))
                .build()

        then: "I see that the values are set correctly"
        with(entity) {
            firstName == 'Marc'
            lastName == 'Smith'
            gender == 'M'
            billingAddress == 0
            shippingAddress == 0
            addresses[0].address1 == 'addr1'
        }
    }


}
