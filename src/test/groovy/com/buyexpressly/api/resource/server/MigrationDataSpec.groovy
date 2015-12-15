package com.buyexpressly.api.resource.server

import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class MigrationDataSpec extends Specification {

    def "a migration data object can be mapped from a json string"() {
        given: "I have a email  in its json representation"
        String json = '{"meta":{},"data":{}}'

        when: "I map it"
        MigrationData data = new ObjectMapper().readValue(json, MigrationData)

        then: "I can see that the values are populated correctly"
        data.data instanceof Customer
        data.meta instanceof Metadata
    }

}
