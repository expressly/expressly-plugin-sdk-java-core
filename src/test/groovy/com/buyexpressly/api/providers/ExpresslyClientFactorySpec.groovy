package com.buyexpressly.api.providers

import com.buyexpressly.api.resource.error.ExpresslyException
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

class ExpresslyClientFactorySpec extends Specification {
    public static final String XLY_SERVER_URL = "http://localhost:8080/api";
    public static final String TEST_UUID = UUID.randomUUID().toString()
    public static final String TEST_PASS = RandomStringUtils.randomAlphabetic(32)
    public static final String RANDOM_TOKEN = String.format("%s:%s", TEST_UUID, TEST_PASS).bytes.encodeBase64().toString()

    ExpresslyClientFactory expresslyClientFactory;

    def "I can create an instance of the xly client factory"() {
        when:
        expresslyClientFactory = new ExpresslyClientFactory(XLY_SERVER_URL, RANDOM_TOKEN)

        then:
        expresslyClientFactory != null
        expresslyClientFactory instanceof ExpresslyClientFactory
        expresslyClientFactory.expresslyEndpoint == XLY_SERVER_URL
        expresslyClientFactory.expresslyApiKey == RANDOM_TOKEN
    }

    def "I can't create an instance of the xly client factory without a properly formatted xly url"() {
        when:
        expresslyClientFactory = new ExpresslyClientFactory("server.com", RANDOM_TOKEN)

        then:
        expresslyClientFactory == null
        thrown(ExpresslyException)
    }
    def "I can't create an instance of the xly client factory without a properly formatted apikey"() {
        when:
        expresslyClientFactory = new ExpresslyClientFactory(XLY_SERVER_URL, RANDOM_TOKEN.substring(10))

        then:
        expresslyClientFactory == null
        thrown(ExpresslyException)
    }

    def "I can make an xly client"() {
        given:
        expresslyClientFactory = new ExpresslyClientFactory(XLY_SERVER_URL, RANDOM_TOKEN)

        when:
        ExpresslyHttpClient client = expresslyClientFactory.makeClient("GET", "endpoint")

        then:
        client != null
        client.requestBuilder.method == "GET"
        client.requestBuilder.uri.toURL().toString() == String.format("%s/%s", XLY_SERVER_URL, "endpoint")
        client.requestBuilder.getFirstHeader("Authorization").toString() == String.format("Authorization: Basic %s", RANDOM_TOKEN)
    }

    def "I can't make an xly client if I pass in an invalid request method"() {
        given:
        expresslyClientFactory = new ExpresslyClientFactory(XLY_SERVER_URL, RANDOM_TOKEN)

        when:
        ExpresslyHttpClient client = expresslyClientFactory.makeClient("INVALID", "endpoint")

        then:
        client == null
        thrown(ExpresslyException)
    }

}


