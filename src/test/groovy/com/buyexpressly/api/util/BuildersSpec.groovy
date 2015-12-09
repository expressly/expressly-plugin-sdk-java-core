package com.buyexpressly.api.util

import com.buyexpressly.api.resource.error.ExpresslyException
import spock.lang.Specification

class BuildersSpec extends Specification {
    public static
    final String API_KEY = "NDUxZDdjOTQtYzFmNy00ZjgwLTljYjktMGE1NGI5MWRmZjk1OkxxcDlsZDl2S05NUTlDd3dFNlVTTFhwYVh4Ymw4eWJn"
    private String pattern = "(.+@.+)"

    def "I can't instantiate a Builders  Object"() {
        when:
        new Builders()

        then:

        !(Builders instanceof Builders)

        ExpresslyException exception = thrown()
        exception.getMessage() == "Builders class cannot be instantiated"
    }

    def "I can validate a String is limited to a given maximum length"() {
        given: "I have an input string"
        String testString = "testString"

        when: "I try to validate it's length"
        Builders.len(testString, "testString", 20)

        then: "I can see no exception was thrown"
        noExceptionThrown()
    }

    def "I don't get an exception when chcking for length if the String is null"() {
        given: "I have an input string"
        String testString = null

        when: "I try to validate it's length"
        Builders.len(testString, "testString", 20)

        then: "I can see no exception was thrown"
        noExceptionThrown()
    }

    def "I don't get an exception when chcking for length if the String is empty"() {
        given: "I have an input string"
        String testString = ""

        when: "I try to validate it's length"
        Builders.len(testString, "testString", 20)

        then: "I can see no exception was thrown"
        noExceptionThrown()
    }

    def "I get an exception if a string is longer than a given maximum length"() {
        given: "I have an input string"
        String testString = "testString"

        when: "I try to validate it's length"
        Builders.len(testString, "testString", 4)

        then: "I can see an exception was thrown"
        ExpresslyException exception = thrown()
        exception.getMessage() == "field [testString] max allowed length is [4], actual length is [10]"
    }

    def "I can validate that a String is matches a pattern"() {
        given: "I have an input string"
        String testString = "test@email.com"

        when: "I try to validate it's pattern"
        Builders.pattern(testString, "testString", pattern)

        then: "I can see no exception was thrown"
        noExceptionThrown()
    }

    def "I don't get an exception when validating a pattern if the String is null"() {
        given: "I have an input string"
        String testString = null

        when: "I try to validate it's pattern"
        Builders.pattern(testString, "testString", pattern)

        then: "I can see no exception was thrown"
        noExceptionThrown()
    }

    def "I don't get an exception when validating a pattern if the String is empty"() {
        given: "I have an input string"
        String testString = ""

        when: "I try to validate it's pattern"
        Builders.pattern(testString, "testString", pattern)

        then: "I can see no exception was thrown"
        noExceptionThrown()
    }

    def "I get an exception when validating a pattern if a string doesn't match the pattern"() {
        given: "I have an input string"
        String testString = "invalid.email"

        when: "I try to validate it's pattern"
        Builders.pattern(testString, "testString", pattern)

        then: "I can see an exception was thrown"
        ExpresslyException exception = thrown()
        exception.getMessage() == "field [testString] value is invalid, should match pattern [(.+@.+)]"
    }

    def "I can validate that a required field is not null"() {
        given: "I have a value"
        def testValue = new Object()
        def otherTestValue = "testValue"

        when: "I check the required value"
        Builders.required(testValue, "testValue")

        and: "The same method works with other types"
        Builders.required(otherTestValue, "otherTestValue")

        then: "I can see the value is available"
        noExceptionThrown()
    }

    def "I get an exception if a required field is null"() {
        given: "I have a value"
        def testValue = null

        when: "I check the required value"
        Builders.required(testValue, "testValue")

        then: "I can see the value is not available, and an appropriate message is associated"
        ExpresslyException exception = thrown()
        exception.getMessage() == "field [testValue] is required"
    }

    def "I can check if a String is not null or empty"() {
        given: "I have a non null/empty value"
        String testValue = "testValue"

        when: "I check for empty/null"
        boolean result = Builders.isNullOrEmpty(testValue)

        then:
        !result
    }

    def "I can check if a String is null or empty"() {
        given: "I have a non null/empty value"
        String testValue = ""

        when: "I check for empty/null"
        boolean emptyResult = Builders.isNullOrEmpty(testValue)
        boolean nullResult = Builders.isNullOrEmpty(testValue)

        then:
        emptyResult
        nullResult
    }

    def "I can evaluate a boolean condition"() {
        when: "I try to evaluate boolean conditions"
        Builders.validate(true, null)

        then: "I can see i get the expected results"
        noExceptionThrown()
    }

    def "I get an exception if the evaluated condition is false"() {
        when: "I try to evaluate boolean conditions"
        Builders.validate(false, null)

        then: "I can see i get the expected results"
        ExpresslyException exception = thrown()
        exception.getMessage() == "Validation condition failed"
    }

    def "I get an exception with a bespoke message if the evaluated condition is false"() {
        given: "I have a particular message to be displayed if the validation fails"
        def expectedMessage = "bespoke validation message"

        when: "I try to evaluate boolean conditions"
        Builders.validate(false, expectedMessage)

        then: "I can see i get the expected results"
        ExpresslyException exception = thrown()
        exception.getMessage() == expectedMessage
    }

    def "I can validate that a field is not null"() {
        given: "I have a value"
        def testValue = new Object()
        def otherTestValue = "testValue"

        when: "I check the required value"
        Builders.requiresNonNull(testValue, "")

        and: "The same method works with other types"
        Builders.requiresNonNull(otherTestValue, "")

        then: "I can see the value is available"
        noExceptionThrown()
    }

    def "I get an exception with a tailored message if a field is null"() {
        given: "I have a null value and a message"
        def testValue = null
        def expectedMessage = "testValue is required to have content"

        when: "I check the required value"
        Builders.requiresNonNull(testValue, expectedMessage)

        then: "I can see the value is not available, and an appropriate message is associated"
        ExpresslyException exception = thrown()
        exception.getMessage() == expectedMessage
    }

    def "I can validate an apiKey" () {
        given: "I have a valid apiKey"
        String apiKey = API_KEY

        when :"I try to validate the apiKey"
        Builders.validateApiKey(apiKey)

        then: "I can see no exception is thrown"
        noExceptionThrown()
    }

    def "The apiKey validation fails if it is empty" () {
        given: "I have a valid apiKey"
        String apiKey = ""

        when :"I try to validate the apiKey"
        Builders.validateApiKey(apiKey)

        then: "I can see no exception is thrown"
        ExpresslyException exception = thrown()
        exception.getMessage() == "invalid expressly api key"
    }

   def "The apiKey validation fails if it is not encoded properly" () {
        given: "I have a valid apiKey"
        String apiKey = API_KEY.substring(0,2)

        when :"I try to validate the apiKey"
        Builders.validateApiKey(apiKey)

        then: "I can see no exception is thrown"
        ExpresslyException exception = thrown()
        exception.getMessage() == "field [FieldToDecode] value is invalid, should match pattern [^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})\$]"
    }

    def "The apiKey validation fails if it is not made up of two components" () {
        given: "I have a valid apiKey"
        String apiKey = EncodingUtils.toBase64("token")

        when :"I try to validate the apiKey"
        Builders.validateApiKey(apiKey)

        then: "I can see no exception is thrown"
        ExpresslyException exception = thrown()
        exception.getMessage() == "invalid expressly api key composition"
    }
    def "The apiKey validation fails if it does not include a properly structured merchantUuid" () {
        given: "I have a valid apiKey"
        String apiKey = EncodingUtils.toBase64(String.format("123:abc"))

        when :"I try to validate the apiKey"
        Builders.validateApiKey(apiKey)

        then: "I can see no exception is thrown"
        ExpresslyException exception = thrown()
        exception.getMessage() == "field [apiKey:[merchantUuid]] value is invalid, should match pattern [[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}]"
    }
    def "The apiKey validation fails if it does not include a properly structured password" () {
        given: "I have a valid apiKey"
        String apiKey = EncodingUtils.toBase64(String.format("%s:%s", UUID.randomUUID().toString(),"abc"))

        when :"I try to validate the apiKey"
        Builders.validateApiKey(apiKey)

        then: "I can see no exception is thrown"
        ExpresslyException exception = thrown()
        exception.getMessage() == "apiKey:[secretPassword] is not valid"
    }

}
