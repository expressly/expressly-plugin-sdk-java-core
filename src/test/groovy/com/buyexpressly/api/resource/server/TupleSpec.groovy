package com.buyexpressly.api.resource.server

import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class TupleSpec extends Specification {
    def "a tuple can be mapped from a json string"() {
        given: "I have a tuple in its json representation"
        String json = '{"field":"twitter","value":"mgsmith57"}'

        when: "I map it"
        Tuple entity = new ObjectMapper().readValue(json, Tuple)

        then: "I can see that the values are populated correctly"
        entity.field == 'twitter'
        entity.value == 'mgsmith57'
    }
}
