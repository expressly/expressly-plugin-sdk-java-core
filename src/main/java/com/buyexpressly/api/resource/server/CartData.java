package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CartData {
    private String couponCode;
    private String productId;

    private CartData() {

    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getProductId() {
        return productId;
    }
}
