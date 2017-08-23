package com.buyexpressly.api.resource.server

import com.buyexpressly.api.resource.error.ExpresslyException
import com.buyexpressly.api.util.ObjectMapperFactory
import spock.lang.Specification

class CustomerSpec extends Specification {
    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
    }

    def "a Customer object can be parsed from a received json string"() {
        given: "I have a Customer in its properly formatted json representation"
        def expectedEmail = 'someone@test.com'
        def expectedCustomerData = customerDataString()

        String json = '{' +
                '"email" : "' + expectedEmail + '",' +
                '"customerData" : ' + expectedCustomerData  +
                '}'

        when: "I map it"
        Customer entity = ObjectMapperFactory.make().readValue(json, Customer)

        then: "I can see that the values are populated correctly"
        entity != null
        entity.email == expectedEmail
        entity.customerData instanceof CustomerData
        entity.customerData.emails.get(0).email == expectedEmail
    }

    def "A customer object can be instantiated via it's builder"(){
        given:"I have an email and a simple customerdata object"
        def expectedEmail = "test@email.com"
        def expectedCustomerData = CustomerData.builder()
                .withFirstName('Marc')
                .withGender('M')
                .withLastName('Smith')
                .addAddress(Address.builder()
                    .withAddress1("address1")
                    .withCity("London")
                    .withZip("SW4 0ZY")
                    .withCountry("GBR").build())
                .build()


        when:"I try to build an instance of customer"
        Customer customer = Customer.build(expectedCustomerData, expectedEmail)

        then: "I can see the customer object was created correctly"
        customer instanceof Customer
        customer.email == expectedEmail
        customer.customerData == expectedCustomerData
    }

    def "I can't create a customer object without an email"(){
        given:"I have a simple customerdata object"
        def expectedCustomerData = CustomerData.builder()
                .withFirstName('Marc')
                .withGender('M')
                .withLastName('Smith')
                .addAddress(Address.builder()
                    .withAddress1("address1")
                    .withCity("London")
                    .withZip("SW4 0ZY")
                    .withCountry("GBR").build())
                .build()


        when:"I try to build an instance of customer"
        Customer customer = Customer.build(expectedCustomerData, null)

        then: "I can see the customer object failed to be created"
        customer == null
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [email] is required"
    }

    def "I can't create a customer object without a customerData object"(){
        given:"I have an email"
        def expectedEmail = "test@email.com"

        when:"I try to build an instance of customer"
        Customer customer = Customer.build(null, expectedEmail)

        then: "I can see the customer object failed to be created"
        customer == null
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [customerData] is required"
    }

    String customerDataString() {
        return '{' +
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
    }
}
