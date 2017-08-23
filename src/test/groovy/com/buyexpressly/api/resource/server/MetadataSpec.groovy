package com.buyexpressly.api.resource.server

import com.buyexpressly.api.util.ObjectMapperFactory
import spock.lang.Specification

class MetadataSpec extends Specification {
    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
    }

    def "a Metadata object can be parsed from a received json string"() {
        given: "I have a Metadata in its properly formatted json representation"
        def expectedSender = 'http://www.shop.com'
        def expectedLocale = 'GBR'
        def expectedField = "lang"
        def expectedValue = "eng"

        def json = """
                {
                  "sender" : "$expectedSender",
                  "locale" : "$expectedLocale",
                  "issuerData" : [
                      {
                        "field" : "$expectedField",
                        "value" : "$expectedValue"
                      }
                    ]
                }
                """

        when: "I map it"
        Metadata entity = ObjectMapperFactory.make().readValue(json, Metadata)

        then: "I can see that the values are populated correctly"
        entity.sender == expectedSender
        entity.locale == expectedLocale
        entity.issuerData.get(0).field == expectedField
        entity.issuerData.get(0).value == expectedValue
    }

    def "I can build a Metadata object"() {
        given:
        def sender = "http://www.shop.com"
        def locale = "GBR"
        def issuerData = Tuple.build("lang", "eng")

        when:
        def metadata = Metadata.build(sender, locale, issuerData)

        then:
        metadata instanceof Metadata
        metadata.sender == sender
        metadata.locale == locale
        metadata.issuerData.get(0) == issuerData
    }

    def "I can build an empty Metadata object"() {
        when: "I try to build an empty metadata object"
        def metadata = Metadata.build(null, null)

        then: "i can see it is created and it's fields initialised."
        metadata instanceof Metadata
        metadata.sender == ""
        metadata.locale == ""
        metadata.issuerData.size() == 0
    }
}
