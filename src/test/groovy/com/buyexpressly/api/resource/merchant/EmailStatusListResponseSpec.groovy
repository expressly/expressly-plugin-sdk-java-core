package com.buyexpressly.api.resource.merchant

import spock.lang.Specification

class EmailStatusListResponseSpec extends Specification {
    def"I can build an EmailStatusListResponse Object"(){
        given:
        EmailStatusListResponse response;
        def expectedExisting = ["a@test.com","b@test.com"];
        def expectedDeleted = ["c@test.com","d@test.com"];
        def expectedPending = ["e@test.com","f@test.com"];

        when:
        response = EmailStatusListResponse.builder().addExisting(expectedExisting).addDeleted(expectedDeleted).addPending(expectedPending).build()

        then:
        response.existing == expectedExisting
        response.deleted == expectedDeleted
        response.pending == expectedPending
    }
    def"I can build an EmailStatusListResponse with a null Existing collection of emails"(){
        given:
        EmailStatusListResponse response;
        def expectedExisting = null;
        def expectedDeleted = ["c@test.com","d@test.com"];
        def expectedPending = ["e@test.com","f@test.com"];

        when:
        response = EmailStatusListResponse.builder().addExisting(expectedExisting).addDeleted(expectedDeleted).addPending(expectedPending).build()

        then:
        response.existing == []
        response.deleted == expectedDeleted
        response.pending == expectedPending
    }
    def"I can build an EmailStatusListResponse with a null Deleted collection of emails"(){
        given:
        EmailStatusListResponse response;
        def expectedExisting = ["a@test.com","b@test.com"];
        def expectedDeleted = null;
        def expectedPending = ["e@test.com","f@test.com"];

        when:
        response = EmailStatusListResponse.builder().addExisting(expectedExisting).addDeleted(expectedDeleted).addPending(expectedPending).build()

        then:
        response.existing == expectedExisting
        response.deleted == []
        response.pending == expectedPending
    }

    def "I can build an EmailStatusListResponse with a null Pending collection of emails"() {
        given:
        EmailStatusListResponse response;
        def expectedExisting = ["a@test.com", "b@test.com"];
        def expectedDeleted = ["c@test.com"];
        def expectedPending = null;

        when:
        response = EmailStatusListResponse.builder().addExisting(expectedExisting).addDeleted(expectedDeleted).addPending(expectedPending).build()

        then:
        response.existing == expectedExisting
        response.deleted == expectedDeleted
        response.pending == []
    }
    def "test build object with some empty arrays"() {
        given:
        EmailStatusListResponse response;
        def expectedExisting = ["a@test.com", "b@test.com"]
        def expectedDeleted = []
        def expectedPending = []

        when:
        response = EmailStatusListResponse.builder().addExisting(expectedExisting).addDeleted(expectedDeleted).addPending(expectedPending).build()

        then:
        response.existing == expectedExisting
        response.deleted == []
        response.pending == []
    }


    def "test build object with all empties"() {
        given:
        EmailStatusListResponse response;

        when:
        response = EmailStatusListResponse.builder().build()

        then:
        response.existing == []
        response.deleted == []
        response.pending == []
    }


}


