package com.buyexpressly.api.RouterMethods

import com.buyexpressly.api.MerchantServiceRoute
import com.buyexpressly.api.resource.error.ExpresslyException

import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpSession

class RouterDisplayPopupSpec extends RouterAbstractRouteSpec {
    private HttpSession session = Mock(HttpSession.class)

    @Override
    MerchantServiceRoute getRouteUnderTest() {
        MerchantServiceRoute.DISPLAY_POPUP
    }

    def "The request is correctly parsed and the response is correctly formatted"() {
        given: "I can retrieve a popup for a campaign customer"
        def expectedCampaignCustomerUuid = UUID.randomUUID().toString()
        def expectedRoute = "/expressly/api/$expectedCampaignCustomerUuid"
        def popup = getPopup()
        def expectedDestination = "expectedDestination"

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that the provider is called with the correct request data"
        1 * request.getSession() >> session
        2 * request.getRequestURI() >> expectedRoute
        1 * expresslyProvider.fetchMigrationConfirmationHtml(expectedCampaignCustomerUuid) >> {
            String requestCampaignCustomerUuid->
                assert requestCampaignCustomerUuid == expectedCampaignCustomerUuid
                popup
        }
        1 * provider.getPopupDestination() >> expectedDestination

        and: "I can see that the response is written correctly"
        1 * session.setAttribute(_ as String, _ as String) >> {
            String attributeName, String attributeContent ->
                assert attributeName == "popupContent"
                attributeContent.replaceAll('\\s*', '') == popup.replaceAll('\\s*', '')
        }
        1 * request.getRequestDispatcher(_ as String) >> {
            String popupDestination ->
                assert  popupDestination == expectedDestination
                Mock(RequestDispatcher.class)
        }
    }

    def "The request redirects to a set location if an exception is thrown"() {
        given: "I can retrieve a popup for a campaign customer"
        def expectedCampaignCustomerUuid = UUID.randomUUID().toString()
        def expectedRoute = "/expressly/api/$expectedCampaignCustomerUuid"
        def popup = getPopup()
        def expectedDestination = "expectedDestination"

        when: "I invoke the route"
        router.route(request, response)

        then: "I see the failed request is handled correctly"
        1 * request.getSession() >> session
        2 * request.getRequestURI() >> expectedRoute
        1 * expresslyProvider.fetchMigrationConfirmationHtml(expectedCampaignCustomerUuid) >> {
            String requestCampaignCustomerUuid->
                assert requestCampaignCustomerUuid == expectedCampaignCustomerUuid
                throw new ExpresslyException("popup request to expressly server failed")
        }
        1 * provider.getHomePageLocation() >> expectedDestination
        1 * response.sendRedirect(expectedDestination)
    }

    String getPopup() {
        """<link rel="stylesheet" type="text/css" href="https://dev.expresslyapp.com/popup/campaign/css/style.css?v=201548">
<div id="xly" class="xly xly-popup">
    <div class="xly xly-container">
        <div class="xly xly-row">
            <div class="xly-item xly-logo">
                <div class="xly-cell xly-bordered">
                    <div style="background: url(https://buyexpressly.com/assets/img/handshake-lg.png) no-repeat center; background-size: contain;"></div>
                </div>
                <div class="xly-cell">
                    <div class="xly-arrows"></div>
                </div>
                <div class="xly-cell xly-bordered">
                    <div style="background: url(http://xXlVH72h32ooGRXK.cloudapp.net/image.png) no-repeat center; background-size: contain;"></div>
                </div>
            </div>
            <div class="xly-item xly-content">
                <div class="top">
                    Welcome unO1Ia
                </div>
                <div class="xly-bottom">
                    <div>
                        <span>Name XXlVH72h32ooGRXK</span>
                        <span class="xly-regular"> will receive the following info</span>
                        <span class="xly-regular"> from</span>
                        <span>Dummy Shop:</span>
                    </div>
                    <span class="xly-regular">your basic details, delivery address, email.</span>
                </div>
            </div>
            <div class="xly-footer">
                <div class="xly-item">
                    <div class="xly-secure">
                        <img src="https://dev.expresslyapp.com/popup/campaign/img/lock.png">
                        This secure step makes your offer easy (<a href="https://buyexpressly.com/#/consumer"
                                                                   target="_blank">learn more</a>).
                        <a href="#" onclick="popupClose(this);return false;">Not unO1Ia?</a>
                    </div>
                    <div class="xly-actions">
                        <a onclick="popupContinue(this);return false;">
                            <div class="xly-accept">OK</div>
                        </a>
                        <div class="xly-loader"><img src="https://dev.expresslyapp.com/popup/campaign/img/ajax-loader.gif"></div>
                        <a href="#" onclick="popupClose(this);return false;">
                            <div class="xly-decline">Cancel</div>
                        </a>
                    </div>
                    <div class="xly-terms">
                        <div class="xly-policy">
                            <a href="http://xXlVH72h32ooGRXK.cloudapp.net/tacs.html" onclick="openTerms(this);return false;" class="xly-dot">Terms</a>&middot;
                            <a href="http://xXlVH72h32ooGRXK.cloudapp.net/policy.html" onclick="openPrivacy(this);return false;" class="xly-dot">Privacy Policy</a>&middot;
                            Powered by
                            <span class="expressly">
                                <a href="https://www.buyexpressly.com">
                                    <img src="https://dev.expresslyapp.com/popup/campaign/img/expressly_logo.jpg" alt="expressly">
                                </a>
                            </span>
                        </div>
                    </div>
                    <div class="xly-clear"></div>
                </div>
            </div>
        </div>
    </div>
</div>"""
    }
}
