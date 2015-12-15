package com.buyexpressly.api.resource.server

import com.buyexpressly.api.resource.error.ExpresslyException
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

    def "a tuple can be instantiated via a builder"() {
        when: "I try to create an instance of a tuple object"
        def tuple = Tuple.build("field", "value")

        then: "I can see i can create a valid tuple object"
        tuple instanceof Tuple
        tuple.field == "field"
        tuple.value == "value"
    }

    def "a tuple can't be instantiated without a field "() {
        when: "I try to create an instance of a tuple object"
        def tuple = Tuple.build(null, "value")

        then: "I can see the object failed to be instantiated"
        tuple == null
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [field in Tuple] is required"
    }

    def "a tuple can't be instantiated without a value "() {
        when: "I try to create an instance of a tuple object"
        def tuple = Tuple.build("field", null)

        then: "I can see the object failed to be instantiated"
        tuple == null
        ExpresslyException expresslyException = thrown()
        expresslyException.message == "field [value in Tuple] is required"
    }

    def "Two tuples with the same field:value pair are equal"() {
        when:"I have two tuples with matching contents"
        def tupleA = Tuple.build("field", "value")
        def tupleB = Tuple.build("field", "value")

        then:"I can see they are equivalent"
        tupleA == tupleB
        tupleA == tupleA
        tupleB == tupleB
    }

    def "Two tuples with different field:value pair are equal"() {
        when:"I have a set of tuples with unmatiching values"
        def tupleA = Tuple.build("field", "valueA")
        def tupleB = Tuple.build("field", "valueB")
        def tupleC = Tuple.build("fieldA", "value")
        def tupleD = Tuple.build("fieldB", "value")

        then:"I can see they are not equal"
        tupleA != ""
        tupleA != tupleB
        tupleC != tupleD
        tupleA != tupleC
        tupleC != tupleB
    }

    def "A toString() call on a tuple returns the contents properly formatted"() {
        when:"I create a tuple"
        def tuple = Tuple.build("field", "value")

        then:"i can see it's to String method prints in an expected formatted way."
        tuple.toString() == "field='field', value='value'"
    }

    def "Hashcode method uses the correct elements in its overriden implementation"(){
        when:"I call .hascode() on a Tuple"
        def tuple = Tuple.build("field", "valueA")

        then:"I can see the haschode is generated properly"
        tuple.hashCode() == Objects.hash("field", "valueA")
    }
}
