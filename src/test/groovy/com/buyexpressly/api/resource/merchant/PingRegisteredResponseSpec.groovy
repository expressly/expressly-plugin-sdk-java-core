package com.buyexpressly.api.resource.merchant

import com.buyexpressly.api.util.ObjectMapperFactory
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class PingRegisteredResponseSpec extends Specification {
    private ObjectMapper om

    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
        om = ObjectMapperFactory.make()
    }

    def "a PingRegisteredResponse object can be built"() {
        when: "I try to build a ping registered response"
        PingRegisteredResponse entity = PingRegisteredResponse.build()

        then: "I can see that the values are populated correctly"
        entity instanceof PingRegisteredResponse
        entity.isRegistered()
    }

    def "I can generate a json string from a ping response object"() {
        when:
        Map result = om.readValue(om.writeValueAsString(PingRegisteredResponse.build()), Map)

        then:
        result.registered
        result.version == 'V2'
        result.lightbox == 'javascript'
        result.platformName == 'Expressly Java SDK'
        result.platformVersion ==~ /\d+\.\d+\.\d+/
    }
}
