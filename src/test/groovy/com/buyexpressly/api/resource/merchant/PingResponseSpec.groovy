package com.buyexpressly.api.resource.merchant

import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class PingResponseSpec extends Specification {
    def "a PingResponse object can be built"() {
        when: "I try to build a ping response"
        PingResponse entity = PingResponse.build()

        then: "I can see that the values are populated correctly"
        entity instanceof PingResponse
        entity.expressly == "Stuff is happening!"

    }

    def "I can generate a json string from a ping response object object"(){
        when:
        def parsed = new ObjectMapper().writeValueAsString(PingResponse.build())

        then:
        parsed == '{"expressly":"Stuff is happening!"}'
    }
 }
