package com.buyexpressly.api.util

import com.buyexpressly.api.resource.error.ExpresslyException
import spock.lang.Specification

class EncodingUtilsSpec extends Specification {
    private final String TO_BE_ENCODED = "String To Be Encoded to base 64";
    private final String TO_BE_DECODED = "U3RyaW5nIFRvIEJlIEVuY29kZWQgdG8gYmFzZSA2NA==";

    def "I can encode to base64"() {
        when: "I use the EncodingUtils class"
        String result = EncodingUtils.toBase64(TO_BE_ENCODED)

        then:"I can verify the string was properly encoded"
        Builders.pattern(result, "result", "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})\$")
        result.equals(TO_BE_DECODED)
    }

    def "I can decode from base64"() {
        given: "Encoding Utils has a valid regex for base64 verification"
        assert EncodingUtils.BASE64_PATTERN == "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})\$"

        when: "I use the EncodingUtils class"
        String result = EncodingUtils.fromBase64(TO_BE_DECODED)

        then:"I can verify the string was properly decoded"
        result.equals(TO_BE_ENCODED)
    }

    def "I can't decode from base64 if the token is not valid"() {
        given: "Encoding Utils has a valid regex for base64 verification"
        assert EncodingUtils.BASE64_PATTERN == "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})\$"

        when: "I use the EncodingUtils class"
        String result = EncodingUtils.fromBase64(TO_BE_DECODED.substring(0, TO_BE_DECODED.length() - 2))

        then:"I can see the decoder threw an error"
        result == null
        ExpresslyException exception = thrown()
        exception.getMessage() == "field [FieldToDecode] value is invalid, should match pattern [^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})\$]"
    }

    def "I can see the validation of the encoding fails with an invalid encoded String" () {
        when: "I use the EncodingUtils class"
        EncodingUtils.validateEncoding("U3RyaW5nIFRvIEJlIEVuY29kZWQgdG8gYmFzZSA2NA", "Base64 Test String")

        then:"I can verify the string failed to be decoded"
        thrown(ExpresslyException)
    }

    def "I can see the validation of the encoding succeeds with an valid encoded String" () {
        when: "I use the EncodingUtils class"
        EncodingUtils.validateEncoding(TO_BE_DECODED, "Base64 Test String")

        then:"I can verify the string is a valid base64 string"
        notThrown(ExpresslyException)
    }

    def "I can't instantiate an Encoding Utils Object"(){
        when:
        EncodingUtils encodingUtils = new EncodingUtils()

        then:

        !(encodingUtils instanceof EncodingUtils)

        ExpresslyException exception = thrown()
        exception.getMessage() == "EncodingUtils cannot be instantiated"
    }

}
