package com.buyexpressly.api.resource.server

import com.buyexpressly.api.util.ObjectMapperFactory
import spock.lang.Specification

class BannerDetailResponseSpec extends Specification {

    void setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
    }

    def "a Banner Detail Response object can be parsed from a received json string"() {
        given: "I have a BannerDetailResponse in its properly formatted json representation"
        def expectedImageUrl = 'http://some.image.url/img.jpg'
        def expectedMigrationLink = 'mgsmith57'

        String json = '{' +
                '"bannerImageUrl" : "' + expectedImageUrl + '",' +
                '"migrationLink" : "' + expectedMigrationLink + '"' +
                '}'

        when: "I map it"
        BannerDetailResponse entity = ObjectMapperFactory.make().readValue(json, BannerDetailResponse)

        then: "I can see that the values are populated correctly"
        entity.bannerImageUrl == expectedImageUrl
        entity.migrationLink == expectedMigrationLink
    }
}
