package com.buyexpressly.api.RouterMethods
import com.buyexpressly.api.MerchantServiceRoute

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

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that the provider is called with the correct request data"
        1 * request.requestURI >> expectedRoute
        1 * provider.popupHandler(request, response, expresslyProvider)

    }
}
