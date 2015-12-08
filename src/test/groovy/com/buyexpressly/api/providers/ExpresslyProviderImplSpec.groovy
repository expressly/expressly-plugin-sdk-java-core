package com.buyexpressly.api.providers

import com.buyexpressly.api.resource.error.ExpresslyException
import com.buyexpressly.api.resource.server.BannerDetailResponse
import com.buyexpressly.api.resource.server.MigrationResponse
import com.buyexpressly.api.resource.server.RegisterPluginRequest
import com.buyexpressly.api.resource.server.SuccessMessageResponse
import com.buyexpressly.api.resource.server.XlyQuery
import org.apache.commons.lang.RandomStringUtils
import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

import java.util.regex.Pattern

class ExpresslyProviderImplSpec extends Specification {
    public static final String XLY_SERVER_URL = "http://mock.api.com";
    public static final String TEST_UUID = UUID.randomUUID().toString()
    public static final String TEST_PASS = RandomStringUtils.randomAlphabetic(32)
    public static final String XLY_KEY = String.format("%s:%s", TEST_UUID, TEST_PASS).bytes.encodeBase64().toString()
    public static final String GET = "GET"
    public static final String POST = "POST"
    public static final String DELETE = "DELETE"

    ExpresslyProviderImpl expresslyProvider;
    ExpresslyHttpClient client;

    def setup() {
        expresslyProvider = new ExpresslyProviderImpl(XLY_SERVER_URL, XLY_KEY)
        expresslyProvider.expresslyClientFactory = Mock(ExpresslyClientFactory.class, constructorArgs: [XLY_SERVER_URL, XLY_KEY])
    }

    def cleanup() {
        expresslyProvider = null
    }

    def "the provider constructor throws an exception if the xly server url is not a valid url" () {
        when: "I try to instantiate an xly provider"
        expresslyProvider = new ExpresslyProviderImpl("www.invalid.url", "")

        then: "I can see the provider failed to be instantiated"
        ExpresslyException exception = thrown()
        "field [expresslyEndpoint] value is invalid, should match pattern [http(s)?://.+]" == exception.message
    }

    def "the provider constructor throws an exception if the xly server apikey is not a validly formatted apikey" () {
        when: "I try to instantiate an xly provider"
        expresslyProvider = new ExpresslyProviderImpl(XLY_SERVER_URL, "invalid xly key")

        then: "I can see the provider failed to be instantiated"
        ExpresslyException exception = thrown()
        "field [expresslyApiKey] value is invalid, should match pattern [^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})\$]" == exception.message
    }

    def "the provider can request a ping to xly"() {
        given: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [GET, ExpresslyApiEndpoint.PING.endpoint, XLY_KEY, XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == GET
                assert endpoint == ExpresslyApiEndpoint.PING.endpoint
                client
        }
        1 * client.call(_ as HashMap<String, String>) >> {
            Map<String, String> responseType ->
                assert responseType instanceof Map
                assert responseType.size() == 0
                [
                        "Server"   : "Live",
                        "DB Status": "Live"
                ]
        }

        when: "I request a ping"
        boolean ping = expresslyProvider.ping();

        then: "I can see the provider treats the response accurately"
        ping
    }

    def "the provider throws an exception if the ping is not successful"() {
        given: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [GET, ExpresslyApiEndpoint.PING.endpoint, XLY_KEY, XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == GET
                assert endpoint == ExpresslyApiEndpoint.PING.endpoint
                client
        }
        1 * client.call(_ as HashMap<String, String>) >> {
            Map<String, String> responseType ->
                assert responseType instanceof Map
                assert responseType.size() == 0
                throw new ExpresslyException("Client wasn't reacheable")
        }

        when: "I try to request a ping from xly"
        boolean ping = expresslyProvider.ping();

        then: "I get a an exception"
        !ping
        thrown(ExpresslyException)
    }

    def "the provider throws an exception if the apibaseurl is not properly formatted"() {
        given: "I have an improperly formatted apibaseurl"
        String requestApiBaseUrl = "test.shop.url/plugins"


        when:
        boolean status = expresslyProvider.install(requestApiBaseUrl);

        then:
        thrown(ExpresslyException)
        !status
    }

    def "the provider can request an installation at xly"() {
        given: "I have a properly formatted apibaseurl"
        String requestApiBaseUrl = "https://test.shop.url/plugins"

        and: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [POST, ExpresslyApiEndpoint.REGISTER.endpoint, XLY_KEY, XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == POST
                assert endpoint == ExpresslyApiEndpoint.REGISTER.endpoint
                client
        }
        1 * client.withRequestBody(_ as XlyQuery) >> {
            XlyQuery query ->
                query instanceof XlyQuery
                ObjectMapper om = new ObjectMapper()
                RegisterPluginRequest request = om.readValue(query.content, RegisterPluginRequest.class)
                request.apiBaseUrl == requestApiBaseUrl
                request.apiKey == XLY_KEY
                request.pluginVersion == "v2"
        }
        1 * client.call(_ as String) >> {
            String responseType ->
                assert responseType instanceof String
                assert responseType.size() == 0
                "204"
        }

        when:
        boolean status = expresslyProvider.install(requestApiBaseUrl);

        then:
        status
    }

    def "the provider throws an xly exception if the client request is unsuccessful"() {
        given: "I have a properly formatted apibaseurl"
        String requestApiBaseUrl = "https://test.shop.url/plugins"

        and: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [POST, ExpresslyApiEndpoint.REGISTER.endpoint, XLY_KEY, XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == POST
                assert endpoint == ExpresslyApiEndpoint.REGISTER.endpoint
                client
        }
        1 * client.withRequestBody(_ as XlyQuery) >> {
            XlyQuery query ->
                query instanceof XlyQuery
                ObjectMapper om = new ObjectMapper()
                RegisterPluginRequest request = om.readValue(query.content, RegisterPluginRequest.class)
                request.apiBaseUrl == requestApiBaseUrl
                request.apiKey == XLY_KEY
                request.pluginVersion == "v2"
        }
        1 * client.call(_ as String) >> {
            String responseType ->
                throw new ExpresslyException("")
        }

        when:
        boolean status = expresslyProvider.install(requestApiBaseUrl);

        then:
        !status
        thrown(ExpresslyException)
    }

    def "the provider can request an uninstall from xly"() {
        given: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                DELETE,
                ExpresslyApiEndpoint.UNINSTALL.getEndpoint().replaceAll(Pattern.quote("{uuid}"), TEST_UUID),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == DELETE
                assert endpoint == ExpresslyApiEndpoint.UNINSTALL.getEndpoint().replaceAll(Pattern.quote("{uuid}"), TEST_UUID)
                client
        }
        1 * client.call(_ as SuccessMessageResponse) >> {
            SuccessMessageResponse responseType ->
                assert responseType instanceof SuccessMessageResponse
                SuccessMessageResponse returnable = new SuccessMessageResponse()
                returnable.success = true
                returnable
        }

        when: "I make a request to uninstall the plugin to the provider"
        boolean uninstall = expresslyProvider.uninstall()

        then: "I can see the request was successful"
        uninstall
    }

    def "if the request to the provider fails, an exception is thrown"() {
        given: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                DELETE,
                ExpresslyApiEndpoint.UNINSTALL.getEndpoint().replaceAll(Pattern.quote("{uuid}"), TEST_UUID),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == DELETE
                assert endpoint == ExpresslyApiEndpoint.UNINSTALL.getEndpoint().replaceAll(Pattern.quote("{uuid}"), TEST_UUID)
                client
        }
        1 * client.call(_ as SuccessMessageResponse) >> {
            SuccessMessageResponse responseType ->
                throw new ExpresslyException("")

        }

        when: "I make a request to uninstall the plugin to the provider"
        boolean uninstall = expresslyProvider.uninstall()

        then: "I can see the request was successful"
        !uninstall
        thrown(ExpresslyException)
    }

    def "I can request the provider to fetch the migration popup html"() {
        given: "I have a valid formatted campaign customer UUID"
        String expectedCustomerUuid = UUID.randomUUID().toString()

        and: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                GET,
                ExpresslyApiEndpoint.POPUP_HTML.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCustomerUuid),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == GET
                assert endpoint == ExpresslyApiEndpoint.POPUP_HTML.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCustomerUuid)
                client
        }
        1 * client.call(_ as String) >> {
            String responseType ->
                assert responseType instanceof String
                responseType.size() == 0
                "success"
        }

        when: "I make a request to the provider for the migration popup"
        String popup = expresslyProvider.fetchMigrationConfirmationHtml(expectedCustomerUuid)

        then: "I can see the request was successful"
        popup == "success"
    }

    def "I can't request the provider to fetch the migration popup html if the client throws an exception"() {
        given: "I have a valid formatted campaign customer UUID"
        String expectedCustomerUuid = UUID.randomUUID().toString()

        and: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                GET,
                ExpresslyApiEndpoint.POPUP_HTML.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCustomerUuid),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == GET
                assert endpoint == ExpresslyApiEndpoint.POPUP_HTML.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCustomerUuid)
                client
        }
        1 * client.call(_ as String) >> {
            String responseType ->
                assert responseType instanceof String
                responseType.size() == 0
                throw new ExpresslyException("")
        }

        when: "I make a request to the provider for the migration popup"
        String popup = expresslyProvider.fetchMigrationConfirmationHtml(expectedCustomerUuid)

        then: "I can see the request was successful"
        popup == null
        thrown(ExpresslyException)
    }

    def "I can't request the provider to fetch the migration popup html if the campaignCustomerUuid is not valid"() {
        given: "I have a uuid for a customer"
        String expectedCustomerUuid = "InvalidUuid"

        when: "I make a request to the provider for the migration popup"
        String popup = expresslyProvider.fetchMigrationConfirmationHtml(expectedCustomerUuid)

        then: "I can see the request has thrown an exception"
        popup == null
        thrown(ExpresslyException)
    }

    def "I can request the provider for the migration customer data"() {
        given: "I have a properly formatted campaign customer uuid"
        String expectedCampaignCustomerUuid = UUID.randomUUID().toString()

        and: "I have a client set up with the correct information"
        MigrationResponse expectedResponse = new MigrationResponse()
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                GET,
                ExpresslyApiEndpoint.CUSTOMER.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCampaignCustomerUuid),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == GET
                assert endpoint == ExpresslyApiEndpoint.CUSTOMER.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCampaignCustomerUuid)
                client
        }
        1 * client.call(_ as MigrationResponse) >> {
            MigrationResponse responseType ->
                assert responseType instanceof MigrationResponse
                expectedResponse
        }

        when: "I make a request to the provider for the migration popup"
        def response = expresslyProvider.fetchMigrationCustomerData(expectedCampaignCustomerUuid)

        then: "I can see the request was executed properly"
        response instanceof MigrationResponse
        response == expectedResponse
    }

    def "I get an exception if the xly server throws an error"() {
        given: "I have a properly formatted campaign customer uuid"
        String expectedCampaignCustomerUuid = UUID.randomUUID().toString()

        and: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                GET,
                ExpresslyApiEndpoint.CUSTOMER.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCampaignCustomerUuid),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == GET
                assert endpoint == ExpresslyApiEndpoint.CUSTOMER.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCampaignCustomerUuid)
                client
        }
        1 * client.call(_ as MigrationResponse) >> {
            MigrationResponse responseType ->
                throw new ExpresslyException("")
        }

        when: "I make a request to the provider for the migration popup"
        def response = expresslyProvider.fetchMigrationCustomerData(expectedCampaignCustomerUuid)

        then: "I can see the request was successful"
        response == null
        thrown(ExpresslyException)
    }

    def "I get an exception if the customer uuid is invalid"() {
        given: "I have a properly formatted campaign customer uuid"
        String expectedCampaignCustomerUuid = "IllegalUuid"

        when: "I make a request to the provider for the migration popup"
        expresslyProvider.fetchMigrationCustomerData(expectedCampaignCustomerUuid)

        then: "I can see an exception is thrown"
        thrown(ExpresslyException)
    }

    def "I can send a request to the xly provider to confirm the migration of a customer"() {
        given: "I have a properly formatted campaign customer uuid"
        String expectedCampaignCustomerUuid = UUID.randomUUID().toString()

        and: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                POST,
                ExpresslyApiEndpoint.CONFIRM_MIGRATION.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCampaignCustomerUuid),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == POST
                assert endpoint == ExpresslyApiEndpoint.CONFIRM_MIGRATION.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCampaignCustomerUuid)
                client
        }
        1 * client.call(_ as SuccessMessageResponse) >> {
            SuccessMessageResponse responseType ->
                assert responseType instanceof SuccessMessageResponse
                SuccessMessageResponse successMessageResponse = new SuccessMessageResponse()
                successMessageResponse.success = true
                successMessageResponse
        }


        when: "I make a request to the provider for the migration popup"
        boolean response = expresslyProvider.finaliseMigrationOfCustomerData(expectedCampaignCustomerUuid)

        then: "I can see the request was properly formatted"
        response
    }

    def "I can't send a request to the xly provider to confirm the migration of a customer with an invalid customer uuid"() {
        given: "I have a properly formatted campaign customer uuid"
        String expectedCampaignCustomerUuid = "InvalidUuid"

        when: "I make a request to the provider for the migration popup"
        boolean response = expresslyProvider.finaliseMigrationOfCustomerData(expectedCampaignCustomerUuid)

        then: "I can see the client throws an exception"
        !response
        thrown(ExpresslyException)
    }

    def "I get an exception when when i try to  request the xly provider to confirm the migration of a customer and xly server returns an error"() {
        given: "I have a properly formatted campaign customer uuid"
        String expectedCampaignCustomerUuid = UUID.randomUUID().toString()

        and: "I have a client set up with the correct information"
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                POST,
                ExpresslyApiEndpoint.CONFIRM_MIGRATION.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCampaignCustomerUuid),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == POST
                assert endpoint == ExpresslyApiEndpoint.CONFIRM_MIGRATION.getEndpoint().replaceAll(Pattern.quote("{uuid}"), expectedCampaignCustomerUuid)
                client
        }
        1 * client.call(_ as SuccessMessageResponse) >> {
            SuccessMessageResponse responseType ->
                assert responseType instanceof SuccessMessageResponse
                throw new ExpresslyException("")
        }

        when: "I make a request to the provider for the migration popup"
        boolean response = expresslyProvider.finaliseMigrationOfCustomerData(expectedCampaignCustomerUuid)

        then: "I can see the request was properly formatted"
        !response
        thrown(ExpresslyException)
    }

    def "I can request a banner from the provider"() {
        given: "I have a valid email address"
        String requestEmail = "a@test.com"

        and: "I have a client set up with the correct information"
        BannerDetailResponse expectedResponse = new BannerDetailResponse()
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                GET,
                ExpresslyApiEndpoint.GET_BANNER.getEndpoint().replaceAll(Pattern.quote("{merchantUuid}"), TEST_UUID),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == GET
                assert endpoint == ExpresslyApiEndpoint.GET_BANNER.getEndpoint().replaceAll(Pattern.quote("{merchantUuid}"), TEST_UUID)
                client
        }
        1 * client.withQueryVariable(_ as String, _ as String) >> {
            String name, String value ->
                assert name == "email"
                assert value == requestEmail
        }
        1 * client.call(_ as BannerDetailResponse) >> {
            BannerDetailResponse responseType ->
                assert responseType instanceof BannerDetailResponse
                expectedResponse
        }

        when: "I request the xly provider for a banner"
        def response = expresslyProvider.getCampaignBanner(requestEmail)

        then: "I can see the request was properly constructed"
        response instanceof BannerDetailResponse
        response == expectedResponse
    }

    def "I can't request the provider for a banner if the email is not properly formatted"() {
        given: "I have an invalid email address"
        String requestEmail = "a.invalid.com"

        when: "I request the xly provider for a banner"
        def response = expresslyProvider.getCampaignBanner(requestEmail)

        then: "I can see the request throws an exception"
        thrown(ExpresslyException)
    }

    def "I get an exception when my request the provider for a banner fails"() {
        given: "I have a valid email address"
        String requestEmail = "a@test.com"

        and: "I have a client set up with the correct information"
        BannerDetailResponse expectedResponse = new BannerDetailResponse()
        client = Spy(ExpresslyHttpClient.class, constructorArgs: [
                GET,
                ExpresslyApiEndpoint.GET_BANNER.getEndpoint().replaceAll(Pattern.quote("{merchantUuid}"), TEST_UUID),
                XLY_KEY,
                XLY_SERVER_URL])
        1 * expresslyProvider.expresslyClientFactory.makeClient(_ as String, _ as String) >> {
            String method, String endpoint ->
                assert method == GET
                assert endpoint == ExpresslyApiEndpoint.GET_BANNER.getEndpoint().replaceAll(Pattern.quote("{merchantUuid}"), TEST_UUID)
                client
        }
        1 * client.withQueryVariable(_ as String, _ as String) >> {
            String name, String value ->
                assert name == "email"
                assert value == requestEmail
        }
        1 * client.call(_ as BannerDetailResponse) >> {
            BannerDetailResponse responseType ->
                throw new ExpresslyException()
        }

        when: "I request the xly provider for a banner"
        def response = expresslyProvider.getCampaignBanner(requestEmail)

        then: "I can see the request was properly constructed"
        response == null;
        thrown(ExpresslyException)
    }
}


