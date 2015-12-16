package com.buyexpressly.api.resource.merchant

import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class PingRegisteredResponseSpec extends Specification {
    def "a PingRegisteredResponse object can be built"() {
        when: "I try to build a ping registered response"
        PingRegisteredResponse entity = PingRegisteredResponse.build()

        then: "I can see that the values are populated correctly"
        entity instanceof PingRegisteredResponse
        entity.isRegistered()
    }

    def "I can generate a json string from a ping response object"(){
        when:
        def parsed = new ObjectMapper().writeValueAsString(PingRegisteredResponse.build())

        then:
        parsed == '{"registered":true}'
    }
 }
