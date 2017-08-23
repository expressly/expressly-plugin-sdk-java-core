package com.buyexpressly.api.resource.server;

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
