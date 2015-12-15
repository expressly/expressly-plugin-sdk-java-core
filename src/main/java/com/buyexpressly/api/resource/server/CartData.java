package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.util.Objects;

//TODO[][]: create full tests for this class, consider if it needs all the comparator stuff.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CartData cartData = (CartData) o;
        return Objects.equals(couponCode, cartData.couponCode)
               && Objects.equals(productId, cartData.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponCode, productId);
    }

    @Override
    public String toString() {
        return new StringBuilder("CartData{")
                .append("couponCode='").append(couponCode).append('\'')
                .append(", productId='").append(productId).append('\'')
                .append('}')
                .toString();
    }
}
