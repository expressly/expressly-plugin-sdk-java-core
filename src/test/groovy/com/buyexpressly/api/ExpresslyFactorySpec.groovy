package com.buyexpressly.api

import com.buyexpressly.api.providers.ExpresslyProviderImpl
import com.buyexpressly.api.resource.error.ExpresslyException
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

class ExpresslyFactorySpec extends Specification {
    private static final String XLY_SERVER_URL = "http://localhost:8080/api";
    private static final String RANDOM_TOKEN = String.format("%s:%s", UUID.randomUUID().toString(), RandomStringUtils.randomAlphabetic(32)).bytes.encodeBase64().toString()

    ExpresslyProviderImpl expresslyProvider;

    def "I can create an expressly plugin factory"() {
        when:
        ExpresslyFactory factory = ExpresslyFactory.createFactory(RANDOM_TOKEN, Mock(MerchantServiceProvider))

        then:
        factory.expresslyApiKey == RANDOM_TOKEN
    }
    def "I can't create an expressly plugin factory without a properly formatted apikey"() {
        when:
        ExpresslyFactory factory = ExpresslyFactory.createFactory("INVALID_api key", Mock(MerchantServiceProvider))

        then:
        factory == null
        ExpresslyException exception = thrown()
        exception.message == "field [FieldToDecode] value is invalid, should match pattern [^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})\$]"
    }

    def "I can request an instance of the expressly provider with a custom expressly endpoint"() {
        when:
        expresslyProvider = ExpresslyFactory.createFactory(RANDOM_TOKEN, Mock(MerchantServiceProvider), XLY_SERVER_URL).buildExpresslyProvider()

        then:
        expresslyProvider.expresslyClientFactory.expresslyEndpoint == XLY_SERVER_URL
        expresslyProvider.expresslyApiKey == RANDOM_TOKEN
    }

    def "I can request an instance of the expressly provider with the production xly endpoint"() {
        when:
        expresslyProvider = ExpresslyFactory.createFactory(RANDOM_TOKEN, Mock(MerchantServiceProvider)).buildExpresslyProvider()

        then:
        expresslyProvider.expresslyClientFactory.expresslyEndpoint == "https://prod.expresslyapp.com/api"
        expresslyProvider.expresslyApiKey == RANDOM_TOKEN
    }

    def "I can request an instance of the merchant router with the production xly endpoint"() {
        def merchantServiceProvider = Mock(MerchantServiceProvider)
        when:
        MerchantServiceRouter router = ExpresslyFactory.createFactory(RANDOM_TOKEN, merchantServiceProvider).buildRouter()

        then:
        router != null
        router.merchantServiceHandler.expresslyProvider.expresslyClientFactory.expresslyEndpoint == "https://prod.expresslyapp.com/api"
        router.merchantServiceHandler.expresslyProvider.expresslyApiKey == RANDOM_TOKEN
        router.merchantServiceHandler.merchantServiceProvider instanceof MerchantServiceProvider
        router.merchantServiceHandler.merchantServiceProvider == merchantServiceProvider
        router.expectedAuthorizationHeader == "Basic $RANDOM_TOKEN"
    }



}


