package com.buyexpressly.api.resource.merchant;

import com.buyexpressly.api.util.Builders;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class InvoiceResponse {
    private final String email;
    private final int orderCount;
    private final BigDecimal preTaxTotal;
    private final BigDecimal postTaxTotal;
    private final BigDecimal tax;
    private final List<InvoiceOrderResponse> orders;

    private InvoiceResponse(Builder builder) {
        this.email = builder.email;
        this.orderCount = builder.orders.size();
        this.preTaxTotal = builder.preTaxTotal;
        this.postTaxTotal = builder.postTaxTotal;
        this.tax = builder.tax;
        this.orders = Collections.unmodifiableList(builder.orders);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEmail() {
        return email;
    }

    public int getOrderCount() {
        return orderCount;
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

    public List<InvoiceOrderResponse> getOrders() {
        return orders;
    }

    public static final class Builder {
        private String email;
        private BigDecimal preTaxTotal;
        private BigDecimal postTaxTotal;
        private BigDecimal tax;
        private final List<InvoiceOrderResponse> orders = new ArrayList<>();

        public Builder withEmail(String email) {
            Builders.len(email, "email", 128);
            this.email = email;
            return  this;
        }

        public Builder withPreTaxTotal(BigDecimal preTaxTotal) {
            this.preTaxTotal = preTaxTotal.setScale(2, BigDecimal.ROUND_UP);
            return  this;
        }

        public Builder withPostTaxTotal(BigDecimal postTaxTotal) {
            this.postTaxTotal = postTaxTotal.setScale(2, BigDecimal.ROUND_UP);
            return  this;
        }

        public Builder withTax(BigDecimal tax) {
            this.tax = tax.setScale(2, BigDecimal.ROUND_UP);
            return  this;
        }

        public Builder add(InvoiceOrderResponse order) {
            orders.add(order);
            return this;
        }

        public InvoiceResponse build() {
            Builders.required(email, "email");
            Builders.required(preTaxTotal, "preTaxTotal");
            Builders.required(postTaxTotal, "postTaxTotal");
            Builders.required(tax, "tax");
            validatePostTaxTotal(postTaxTotal, orders);
            return new InvoiceResponse(this);
        }

        private void validatePostTaxTotal(BigDecimal postTaxTotal, List<InvoiceOrderResponse> orders) {
            BigDecimal total = BigDecimal.ZERO;
            total = total.setScale(2, BigDecimal.ROUND_UP);
            for (InvoiceOrderResponse order : orders) {
                total = total.add(order.getPostTaxTotal());
            }
            Builders.validate(total.equals(postTaxTotal), String.format(
                    "invoice total [%s] does not not match sum of orders' totals [%s]",
                    postTaxTotal, total));
        }
    }
}
