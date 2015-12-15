package com.buyexpressly.api.resource.server

import com.buyexpressly.api.resource.error.ExpresslyException
import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class EmailSpec extends Specification {
    def "an email can be mapped from a json string"() {
        given: "I have a email  in its json representation"
        String json = '{"alias":"personal","email":"email@test.com"}'

        when: "I map it"
        Email email = new ObjectMapper().readValue(json, Email)

        then: "I can see that the values are populated correctly"
        email.alias == "personal"
        email.email == 'email@test.com'
    }

    def "an email object can be built" () {
        when: "I try to build a email object"
        Email email = Email.builder()
                .withAlias("personal")
                .withEmail("email@test.com")
                .build()

        then: "I can see that the values are populated correctly"
        email.alias == "personal"
        email.email == 'email@test.com'
    }

    def "an email object can be built just with an email" () {
        when: "I try to build an email object"
        Email email = Email.builder()
                .withEmail("test@email.com")
                .build()

        then: "I can see that the values are populated correctly"
        email.alias == null
        email.email == 'test@email.com'
    }

    def "I can't build an email object without an email"(){
        when: "I try to build an Email object"
        Email email = Email.builder()
                .withAlias("persoanl")
                .build()

        then:"I can see an error occurs"
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [email] is required"
    }
}
