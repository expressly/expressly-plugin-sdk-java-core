package com.buyexpressly.api.RouterMethods

import com.buyexpressly.api.ExpresslyProvider
import com.buyexpressly.api.MerchantServiceProvider
import com.buyexpressly.api.MerchantServiceRoute
import com.buyexpressly.api.MerchantServiceRouter
import com.buyexpressly.api.util.ObjectMapperFactory
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract class RouterAbstractRouteSpec extends Specification {
    protected static final String API_KEY = "NDUxZDdjOTQtYzFmNy00ZjgwLTljYjktMGE1NGI5MWRmZjk1OkxxcDlsZDl2S05NUTlDd3dFNlVTTFhwYVh4Ymw4eWJn"

    protected MerchantServiceRouter router
    protected MerchantServiceProvider provider
    protected ExpresslyProvider expresslyProvider
    protected HttpServletRequest request
    protected HttpServletResponse response
    protected ObjectMapper objectMapper
    private StringWriter out = new StringWriter()

    abstract MerchantServiceRoute getRouteUnderTest()

    String getRouteUri() {
        routeUnderTest.uriRegex
    }

    String getResponseString() {
        out.toString()
    }

    def setup() {
        ObjectMapperFactory.failOnUnknownProperties = true
        this.objectMapper = ObjectMapperFactory.make()
        this.provider = Mock(MerchantServiceProvider)
        this.expresslyProvider = Mock(ExpresslyProvider)
        this.router = new MerchantServiceRouter(API_KEY, provider, expresslyProvider)
        request = Mock(HttpServletRequest)
        response = Mock(HttpServletResponse)

        _ * request.getRequestURI() >> routeUri
        1 * request.getMethod() >> routeUnderTest.httpMethod
        _ * request.getHeader("Authorization") >> "Basic ${API_KEY}"
        _ * response.getWriter() >> new PrintWriter(out)
    }
}
