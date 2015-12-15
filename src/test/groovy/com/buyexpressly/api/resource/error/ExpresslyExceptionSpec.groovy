package com.buyexpressly.api.resource.error

import spock.lang.Specification;

class ExpresslyExceptionSpec extends Specification{
    def "I can throw an expressly exception with a message"() {
        given:"I have an exception message setup"
        def exceptionMessage = "message"

        when:
        throw new ExpresslyException(exceptionMessage)

        then:
        ExpresslyException thrownException = thrown()
        thrownException.message == exceptionMessage
    }

}
