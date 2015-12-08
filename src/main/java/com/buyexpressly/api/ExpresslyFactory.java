package com.buyexpressly.api;

import com.buyexpressly.api.providers.ExpresslyProviderImpl;
import com.buyexpressly.api.util.Builders;

public final class ExpresslyFactory {
    private static final String PROD_XLY_URL = "https://prod.expresslyapp.com/api";

    private final String expresslyApiKey;
    private final String expresslyEndpoint;
    private final MerchantServiceProvider merchantServiceProvider;

    private ExpresslyFactory(String expresslyApiKey, MerchantServiceProvider provider, String expresslyEndpoint) {
        this.expresslyApiKey = expresslyApiKey;
        this.merchantServiceProvider = provider;
        this.expresslyEndpoint = expresslyEndpoint;
    }

    public static ExpresslyFactory createFactory(String expresslyApiKey, MerchantServiceProvider provider) {
        return createFactory(expresslyApiKey, provider, PROD_XLY_URL);
    }

    public static ExpresslyFactory createFactory(String expresslyApiKey, MerchantServiceProvider provider, String expresslyEndpoint) {
        Builders.validateApiKey(expresslyApiKey);
        return new ExpresslyFactory(expresslyApiKey, provider, expresslyEndpoint);
    }

    public ExpresslyProvider buildExpresslyProvider() {
        return ExpresslyProviderImpl.create(expresslyEndpoint, expresslyApiKey);
    }

    public MerchantServiceRouter buildRouter() {
        return new MerchantServiceRouter(expresslyApiKey, merchantServiceProvider, buildExpresslyProvider());
    }
}

