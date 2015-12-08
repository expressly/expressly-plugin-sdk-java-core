package com.buyexpressly.api.RouterMethods

import com.buyexpressly.api.MerchantServiceRoute
import com.buyexpressly.api.resource.merchant.InvoiceListRequest
import com.buyexpressly.api.resource.merchant.InvoiceOrderResponse
import com.buyexpressly.api.resource.merchant.InvoiceResponse
import org.joda.time.DateTime
import org.joda.time.LocalDate

class RouterGetInvoicesSpec extends RouterAbstractRouteSpec {

    @Override
    MerchantServiceRoute getRouteUnderTest() {
        MerchantServiceRoute.GET_INVOICES
    }

    def "The request is correctly parsed and the response is correctly formatted"() {
        given: "I have a valid request route"
        1 * request.getReader() >> new BufferedReader(new StringReader(
                """
                    {
                        "customers": [
                            {
                                "email": "john.smith@gmail.com",
                                "from": "2015-07-01",
                                "to": "2015-08-01"
                            }
                        ]
                    }
            """))

        when: "I invoke the route"
        router.route(request, response)

        then: "I see that the provider is called with the correct request data"
        1 * provider.getInvoices(_ as InvoiceListRequest) >> { InvoiceListRequest request ->
            assert request.customers.size() == 1;
            with(request.customers.get(0)) {
                email == 'john.smith@gmail.com'
                from == LocalDate.parse("2015-07-01")
                to == LocalDate.parse("2015-08-01")
            }

            [InvoiceResponse.builder()
                        .setEmail("john.smith@gmail.com")
                        .setPostTaxTotal(110.0G)
                        .setPreTaxTotal(100.0G)
                        .setTax(10.0G)
                        .add(InvoiceOrderResponse.builder()
                            .setOrderId('ORDER-5321311')
                            .setOrderDate(DateTime.parse("2015-07-10T11:42:00+01:00").toLocalDate())
                            .setCurrency('GBP')
                            .setItemCount(2)
                            .setCoupon("COUPON")
                            .setPreTaxTotal(100.0G)
                            .setTax(10.0G)
                            .setPostTaxTotal(110.0G)
                            .build())
                        .build()]
        }

        and: "I can see that the response is written correctly"
        responseString.replaceAll('\\s*', '') == """
                {"invoices":[{"email":"john.smith@gmail.com","orderCount":1,"preTaxTotal":100.00,"postTaxTotal":110.00,"tax":10.00,"orders":[{"id":"ORDER-5321311","date":"2015-07-10","itemCount":2,"coupon":"COUPON","currency":"GBP","preTaxTotal":100.00,"postTaxTotal":110.00,"tax":10.00}]}]}
            """.replaceAll('\\s*', '')
    }
}
