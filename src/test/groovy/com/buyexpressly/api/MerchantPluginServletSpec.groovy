package com.buyexpressly.api

import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

import javax.servlet.ServletConfig
import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MerchantPluginServletSpec extends Specification {
    private static final String XLY_KEY = String.format("%s:%s", UUID.randomUUID().toString(), RandomStringUtils.randomAlphabetic(32)).bytes.encodeBase64().toString()
    private MerchantPluginServlet servlet

    private MerchantServiceRouter router
    private ServletContext servletContext
    private ServletConfig servletConfig

    def setup() {
        servletContext = Mock(ServletContext)
        servletConfig = Mock(ServletConfig)
        servletConfig.servletContext >> servletContext
        router = Spy (MerchantServiceRouter.class, constructorArgs: [XLY_KEY, Mock(MerchantServiceProvider), Mock(ExpresslyProvider)])
        servlet = new MerchantPluginServlet()
    }

    def "if the servlet context does not contain the router as an attribute an error is thrown"() {
        when: "I initialise the servlet"
        servlet.init(servletConfig)

        then: "I see an attempt to get the router"
        1 * servletContext.getAttribute(MerchantServiceRouter.class.name)

        and: "I see that an exception is thrown when it is not found"
        NullPointerException exception = thrown()
        exception.message == 'Instance of [com.buyexpressly.api.MerchantServiceRouter] not found under ServletContext attribute [com.buyexpressly.api.MerchantServiceRouter]'

    }

    def "if the servlet context contains a router than the servlet is initialised correctly"() {
        when: "I initialise the servlet"
        servlet.init(servletConfig)

        then: "I see an attempt to get the router"
        1 * servletContext.getAttribute(MerchantServiceRouter.class.name) >> router

        and: "that the member variable is set correctly"
        noExceptionThrown()
        servlet.router == router
    }

    def "when the servlet is called it delegates to the router"() {
        given: "the servlet is initialised"
        1 * servletContext.getAttribute(MerchantServiceRouter.class.name) >> router
        servlet.init(servletConfig)

        and: "I have a valid request"
        def request = Mock(HttpServletRequest)

        when: "I call the service"
        servlet.service(request, Mock(HttpServletResponse))

        then: "I see that the router is called"
        1 * router.route(_ as HttpServletRequest, _ as HttpServletResponse) >> {
            HttpServletRequest servletRequest, HttpServletResponse response->
                assert servletRequest == request
        }

    }

    def "when the servlet is called ... an exception is thrown"() {
        given: "the servlet is initialised"
        1 * servletContext.getAttribute(MerchantServiceRouter.class.name) >> router
        servlet.init(servletConfig)

        when: "I call the service"
        servlet.service(Mock(HttpServletRequest), Mock(HttpServletResponse))

        then: "I see that the router is called"
        IllegalArgumentException e = thrown()
        e.message == 'invalid expressly merchant route, httpMethod=null, uri=null'

    }
}
