package com.buyexpressly.api
import com.buyexpressly.api.resource.merchant.InvoiceListRequest
import com.buyexpressly.api.resource.merchant.InvoiceResponse
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MerchantServiceRouterSpec extends Specification {
    private static final String API_KEY = String.format("%s:%s", UUID.randomUUID().toString(), RandomStringUtils.randomAlphabetic(32)).bytes.encodeBase64().toString()

    private MerchantServiceRouter router
    private MerchantServiceProvider provider
    private ExpresslyProvider expresslyProvider
    private HttpServletRequest request
    private HttpServletResponse response

    def setup() {
        this.provider = Mock(MerchantServiceProvider)
        this.expresslyProvider = Mock(ExpresslyProvider)
        this.router = new MerchantServiceRouter(API_KEY, provider, expresslyProvider)
        request = Mock(HttpServletRequest)
        response = Mock(HttpServletResponse)
    }

    def "If authentication fails because no Authorization header is set then a 401 is returned"() {
        given: "I have a valid request route"
        1 * request.getRequestURI() >> MerchantServiceRoute.GET_INVOICES.uriRegex
        1 * request.getMethod() >> MerchantServiceRoute.GET_INVOICES.httpMethod

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that authorisation is checked and fails correctly"
        1 * request.getHeader("Authorization")
        1 * response.sendError(401)
        0 * provider.getInvoices(_ as InvoiceListRequest)
    }

    def "If authentication fails because of an incorrect Authorization header then a 401 is returned"() {
        given: "I have a valid request route"
        1 * request.getRequestURI() >> MerchantServiceRoute.GET_INVOICES.uriRegex
        1 * request.getMethod() >> MerchantServiceRoute.GET_INVOICES.httpMethod

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that authorisation is checked and fails correctly"
        1 * request.getHeader("Authorization") >> "Basic INVALID"
        1 * response.sendError(401)
        0 * provider.getInvoices(_ as InvoiceListRequest)
    }

    def "If authentication succeeds then a 401 is not returned"() {
        given: "I have a valid request route"
        1 * request.getRequestURI() >> MerchantServiceRoute.GET_INVOICES.uriRegex
        1 * request.getMethod() >> MerchantServiceRoute.GET_INVOICES.httpMethod
        1 * request.getReader() >> new BufferedReader(new StringReader('{"customers": [{"email" : "a@mail.com"}]}'))
        def invoiceResponses = [
                InvoiceResponse.builder()
                        .setEmail("a@mail.com")
                        .setPostTaxTotal(new BigDecimal(0))
                        .setPreTaxTotal(new BigDecimal(0))
                        .setTax(new BigDecimal(0))
                        .build()]

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that authorisation is checked and allows the provider to be called"
        1 * request.getHeader("Authorization") >> "Basic ${API_KEY}"
        0 * response.sendError(401)

        1 * provider.getInvoices(_ as InvoiceListRequest) >> invoiceResponses
        1 * response.getWriter() >> new PrintWriter(new StringWriter())

    }

    def "If authentication is not required then a 401 is not returned if the header is missing"() {
        given: "I have a valid request route"
        1 * request.getRequestURI() >> MerchantServiceRoute.PING.uriRegex
        1 * request.getMethod() >> MerchantServiceRoute.PING.httpMethod
        StringWriter out = new StringWriter();

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that authorisation is not checked and the provider is called"
        0 * request.getHeader("Authorization")
        0 * response.sendError(401)
        _ * response.getWriter() >> new PrintWriter(out)
        out.toString().replaceAll('\\s*', '') == '{ "expressly": "Stuff is happening!" }'.replaceAll('\\s*', '')
    }

}
