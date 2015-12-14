package com.buyexpressly.api.providers

import com.buyexpressly.api.resource.error.ExpresslyException
import com.buyexpressly.api.resource.server.RegisterPluginRequest
import com.buyexpressly.api.resource.server.SuccessMessageResponse
import com.buyexpressly.api.resource.server.ExpresslyQuery
import org.apache.commons.lang.RandomStringUtils
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.ResponseHandler
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.codehaus.jackson.map.ObjectMapper
import spock.lang.Specification

class ExpresslyHttpClientSpec extends Specification {
    public static final String XLY_SERVER_URL = "http://mock.api.com";
    public static final String TEST_UUID = UUID.randomUUID().toString()
    public static final String TEST_PASS = RandomStringUtils.randomAlphabetic(32)
    public static final String XLY_KEY = String.format("%s:%s", TEST_UUID, TEST_PASS).bytes.encodeBase64().toString()
    public static final String GET = "GET"
    public static final String POST = "POST"
    public static final String DELETE = "DELETE"

    ExpresslyHttpClient client

    def "I can instantiate the Expressly Http client"() {
        given: "I have a valid method, uri, apikey and server url"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL

        when: "I try to instantiate the client"
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)

        then: "I can see it was created correctly"
        client != null
        client instanceof ExpresslyHttpClient
        client.requestBuilder.method == "GET"
        client.requestBuilder.uri.toURL().toString() == String.format("%s/%s", XLY_SERVER_URL, "endpoint")
        client.requestBuilder.getFirstHeader("Authorization").toString() == String.format("Authorization: Basic %s", XLY_KEY)
    }

    def "I can add a query variable to my requestBuilder"() {
        given: "i have a client"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)

        and: "I have a valid name and value"
        String name = "name"
        String value = "value"

        when: "I try to add a query variable to my request"
        client.withQueryVariable(name, value)

        then: "I can see my request includes the given name and value"
        client.requestBuilder.parameters != null
        !client.requestBuilder.parameters.isEmpty()
        client.requestBuilder.parameters.get(0).getName() == name
        client.requestBuilder.parameters.get(0).getValue() == value

    }

    def "I can add more than one query variable to my requestBuilder"() {
        given: "i have a client"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)

        and: "I have a valid name and value"
        String name = "name"
        String value = "value"
        String name1 = "name1"
        String value1 = "value1"

        when: "I try to add a query variable to my request"
        client.withQueryVariable(name, value)
        client.withQueryVariable(name1, value1)

        then: "I can see my request includes the given name and value"
        client.requestBuilder.parameters != null
        client.requestBuilder.parameters.size() == 2
        client.requestBuilder.parameters.get(0).getName() == name
        client.requestBuilder.parameters.get(0).getValue() == value
        client.requestBuilder.parameters.get(1).getName() == name1
        client.requestBuilder.parameters.get(1).getValue() == value1
    }

    def "I can add a request body to my requestBuilder"() {
        given: "i have a client"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)

        and: "I have an entity"
        RegisterPluginRequest query = new RegisterPluginRequest(XLY_KEY, XLY_SERVER_URL, "v2");
        ExpresslyQuery xlyQuery = ExpresslyQuery.toJsonEntity(query)

        when: "I try to add an entity to my request builder"
        client.withRequestBody(xlyQuery)

        then: "I can see my request builder includes an enitity"
        HttpEntity requestEntity = client.requestBuilder.getEntity()
        requestEntity != null
        ObjectMapper mapper = new ObjectMapper()
        RegisterPluginRequest requestedQuery = mapper.readValue(requestEntity.content, RegisterPluginRequest.class)
        requestedQuery.apiBaseUrl == XLY_SERVER_URL
        requestedQuery.apiKey == XLY_KEY
        requestedQuery.pluginVersion == "v2"
    }

    def "I can use a bespoke client in my ExpresslyHttpClient"() {
        given: "i have an expressly http client"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)

        and: "I have an http client"
        CloseableHttpClient bespokeClient = HttpClients.createDefault()

        when: "I try to add a bespoke client to my expressly client "
        client.withHttpClient(bespokeClient)

        then: "I can see my expressly http client is using the bespoke client"
        client.httpClient == bespokeClient
    }

    def "I can make a call"() {
        given: "i have an expressly http client"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)
        CloseableHttpClient mockClient = Mock(CloseableHttpClient)
        client.withHttpClient(mockClient)
        client.requestBuilder = Mock(RequestBuilder)
        def expectedUriRquest = Mock(HttpUriRequest)

        when: "I make a call"
        client.call(String)

        then: "I can see the call is handled correctly"
        1 * client.requestBuilder.build() >> expectedUriRquest
        1 * mockClient.execute(_ as HttpUriRequest, _ as ResponseHandler) >> {
            HttpUriRequest uriRequest, ResponseHandler responseHandler ->
                assert uriRequest == expectedUriRquest
                responseHandler instanceof ResponseHandler<String>
        }
    }

    def "I throw an exception if the response has a status equal or above 400"() {
        given: "i have an expressly http client"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)
        CloseableHttpClient mockClient = Mock(CloseableHttpClient)
        client.withHttpClient(mockClient)
        client.requestBuilder = Mock(RequestBuilder)
        ResponseHandler handler = client.buildResponseHandler(String)
        HttpResponse response = Mock(HttpResponse)
        def statusLine = Mock(StatusLine.class)
        _ * response.getStatusLine() >> statusLine
        _ * statusLine.getStatusCode() >> 400
        _ * statusLine.getReasonPhrase() >> "test exception"
        _ * response.getEntity() >> Mock(HttpEntity)


        when:
        handler.handleResponse(response)


        then:
        ExpresslyException exception = thrown()
        exception.getMessage() == "Error occurred in request to Expressly Server. StatusCode=400, Detail=test exception ResponseContent=null"

    }

    def "I the status code if the entity is empty"() {
        given: "i have an expressly http client"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)
        CloseableHttpClient mockClient = Mock(CloseableHttpClient)
        client.withHttpClient(mockClient)
        client.requestBuilder = Mock(RequestBuilder)
        ResponseHandler handler = client.buildResponseHandler(String)
        HttpResponse response = Mock(HttpResponse)
        def statusLine = Mock(StatusLine.class)
        _ * response.getStatusLine() >> statusLine
        _ * statusLine.getStatusCode() >> 200
        _ * response.getEntity() >> Mock(HttpEntity)

        when:
        String received = handler.handleResponse(response)

        then:
        received == "200"
    }

    def "If the content type of the response is html, i get a string of the content in return"() {
        given: "i have an expressly http client"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)
        CloseableHttpClient mockClient = Mock(CloseableHttpClient)
        client.withHttpClient(mockClient)
        client.requestBuilder = Mock(RequestBuilder)
        ResponseHandler handler = client.buildResponseHandler(String)
        HttpResponse response = Mock(HttpResponse)
        def statusLine = Mock(StatusLine.class)
        _ * response.getStatusLine() >> statusLine
        _ * statusLine.getStatusCode() >> 200
        def httpEntity = EntityBuilder.create().setContentType(ContentType.TEXT_HTML).setText("html_content").build()
        _ * response.getEntity() >> httpEntity


        when:
        String received = handler.handleResponse(response)

        then:
        received == "html_content"
    }

    def "If the content type of the response is json, i get an object of the content in return"() {
        given: "i have an expressly http client"
        String method = GET
        String uri = "endpoint"
        String apiKey = XLY_KEY
        String expresslyEndpoint = XLY_SERVER_URL
        client = new ExpresslyHttpClient(method, uri, apiKey, expresslyEndpoint)
        CloseableHttpClient mockClient = Mock(CloseableHttpClient)
        client.withHttpClient(mockClient)
        client.requestBuilder = Mock(RequestBuilder)
        ResponseHandler handler = client.buildResponseHandler(SuccessMessageResponse)
        HttpResponse response = Mock(HttpResponse)
        def statusLine = Mock(StatusLine.class)
        _ * response.getStatusLine() >> statusLine
        _ * statusLine.getStatusCode() >> 200
        String expectedSuccessMessage = generateSuccessMessage()
        def httpEntity = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(expectedSuccessMessage).build()
        _ * response.getEntity() >> httpEntity


        when:
        SuccessMessageResponse received = handler.handleResponse(response)

        then:
        received.msg == "success"
        received.success
    }

    String generateSuccessMessage() {
        return """
    {
        "success" : true,
        "msg" : "success"
    }
    """
    }
}
