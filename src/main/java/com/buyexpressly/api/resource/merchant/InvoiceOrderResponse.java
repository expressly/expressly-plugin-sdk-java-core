package com.buyexpressly.api.resource.merchant;

import com.buyexpressly.api.util.Builders;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

public final class InvoiceOrderResponse {

    private final String id;
    private final LocalDate date;
    private final int itemCount;
    private final String coupon;
    private final String currency;
    private final BigDecimal preTaxTotal;
    private final BigDecimal postTaxTotal;
    private final BigDecimal tax;

    private InvoiceOrderResponse(Builder builder) {
        this.id = builder.orderId;
        this.date = builder.orderDate;
        this.coupon = builder.coupon;
        this.currency = builder.currency;
        this.itemCount = builder.itemCount;
        this.preTaxTotal = builder.preTaxTotal;
        this.postTaxTotal = builder.postTaxTotal;
        this.tax = builder.tax;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getOrderId() {
        return id;
    }

    public LocalDate getOrderDate() {
        return date;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getCurrency() {
        return currency;
    }

    public int getItemCount() {
        return itemCount;
    }

    public BigDecimal getPreTaxTotal() {
        return preTaxTotal;
    }

    public BigDecimal getPostTaxTotal() {
        return postTaxTotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public static final class Builder {
        private String orderId;
        private LocalDate orderDate;
        private String coupon;
        private String currency;
        private int itemCount;
        private BigDecimal preTaxTotal;
        private BigDecimal postTaxTotal;
        private BigDecimal tax;

        public Builder setOrderId(String orderId) {
            Builders.len(orderId, "orderId", 16);
            this.orderId = orderId;
            return this;
        }

        public Builder setOrderDate(LocalDate orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder setCoupon(String coupon) {
            Builders.len(coupon, "coupon", 48);
            this.coupon = coupon;
            return this;
        }

        public Builder setCurrency(String currency) {
            Builders.pattern(currency, "currency", "[A-Z]{3}");
            this.currency = currency;
            return this;
        }

        public Builder setItemCount(int itemCount) {
            this.itemCount = itemCount;
            return this;
        }

        public Builder setPreTaxTotal(BigDecimal preTaxTotal) {
            this.preTaxTotal = preTaxTotal.setScale(2, BigDecimal.ROUND_UP);
            return this;
        }

        public Builder setPostTaxTotal(BigDecimal postTaxTotal) {
            this.postTaxTotal = postTaxTotal.setScale(2, BigDecimal.ROUND_UP);
            return this;
        }

        public Builder setTax(BigDecimal tax) {
            this.tax = tax.setScale(2, BigDecimal.ROUND_UP);
            return this;
        }

        public InvoiceOrderResponse build() {
            Builders.required(orderId, "orderId");
            Builders.required(orderDate, "orderDate");
            Builders.required(currency, "currency");
            Builders.required(preTaxTotal, "preTaxTotal");
            Builders.required(postTaxTotal, "postTaxTotal");
            Builders.required(tax, "tax");
            Builders.validate(preTaxTotal.add(tax).equals(postTaxTotal), "preTaxTotal + tax must equal postTaxTotal");
            return new InvoiceOrderResponse(this);
        }
    }
}
