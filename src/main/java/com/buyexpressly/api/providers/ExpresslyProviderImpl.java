package com.buyexpressly.api.providers;

import com.buyexpressly.api.ExpresslyProvider;
import com.buyexpressly.api.resource.error.ExpresslyException;
import com.buyexpressly.api.resource.server.BannerDetailResponse;
import com.buyexpressly.api.resource.server.MigrationResponse;
import com.buyexpressly.api.resource.server.RegisterPluginRequest;
import com.buyexpressly.api.resource.server.SuccessMessageResponse;
import com.buyexpressly.api.resource.server.XlyQuery;
import com.buyexpressly.api.util.Builders;
import com.buyexpressly.api.util.EncodingUtils;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class ExpresslyProviderImpl implements ExpresslyProvider {
    private String expresslyApiKey;
    private ExpresslyClientFactory expresslyClientFactory;

    private ExpresslyProviderImpl(String expresslyEndpoint, String expresslyApiKey) {
        Builders.pattern(expresslyEndpoint, "expresslyEndpoint", "http(s)?://.+");
        Builders.validateApiKey(expresslyApiKey);
        expresslyClientFactory = new ExpresslyClientFactory(expresslyEndpoint, expresslyApiKey);
        this.expresslyApiKey = expresslyApiKey;
    }

    public static ExpresslyProvider create(String serverUrl, String expresslyApiKey) {
        return new ExpresslyProviderImpl(serverUrl, expresslyApiKey);
    }

    @Override
    public boolean ping() throws IOException, ExpresslyException {
        ExpresslyHttpClient client = expresslyClientFactory.makeClient(HttpGet.METHOD_NAME, ExpresslyApiEndpoint.PING.getEndpoint());
        Map<String, String> response = client.call(new HashMap<String, String>());
        return "Live".equals(response.get("Server")) && "Live".equals(response.get("DB Status"));
    }

    @Override
    public boolean install(String apiBaseUrl) throws IOException {
        Builders.pattern(apiBaseUrl, "apiBaseUrl", "http(s)?://.+");
        RegisterPluginRequest query = new RegisterPluginRequest(expresslyApiKey, apiBaseUrl, "v2");
        ExpresslyHttpClient client = expresslyClientFactory.makeClient(HttpPost.METHOD_NAME, ExpresslyApiEndpoint.REGISTER.getEndpoint());
        client.withRequestBody(XlyQuery.toJsonEntity(query));
        String response = client.call("");
        return "204".equals(response);
    }

    @Override
    public boolean uninstall() throws IOException {
        ExpresslyHttpClient client = expresslyClientFactory.makeClient(
                HttpDelete.METHOD_NAME,
                buildEndpoint("{uuid}", EncodingUtils.fromBase64(expresslyApiKey).split(":")[0], ExpresslyApiEndpoint.UNINSTALL));
        return client.call(new SuccessMessageResponse()).isSuccess();

    }

    @Override
    public String fetchMigrationConfirmationHtml(String campaignCustomerUuid) throws IOException {
        Builders.pattern(campaignCustomerUuid, "campaignCustomerUuid", "([A-z0-9\\-]{36})");
        ExpresslyHttpClient client = expresslyClientFactory.makeClient(
                HttpGet.METHOD_NAME,
                buildEndpoint("{uuid}", campaignCustomerUuid, ExpresslyApiEndpoint.POPUP_HTML));
        return client.call("");
    }

    @Override
    public MigrationResponse fetchMigrationCustomerData(String campaignCustomerUuid) throws IOException {
        Builders.pattern(campaignCustomerUuid, "campaignCustomerUuid", "([A-z0-9\\-]{36})");
        ExpresslyHttpClient client = expresslyClientFactory.makeClient(
                HttpGet.METHOD_NAME,
                buildEndpoint("{uuid}", campaignCustomerUuid, ExpresslyApiEndpoint.CUSTOMER));
        return client.call(new MigrationResponse());
    }

    @Override
    public boolean finaliseMigrationOfCustomerData(String campaignCustomerUuid) throws IOException {
        Builders.pattern(campaignCustomerUuid, "campaignCustomerUuid", "([A-z0-9\\-]{36})");
        ExpresslyHttpClient client = expresslyClientFactory.makeClient(
                HttpPost.METHOD_NAME,
                buildEndpoint("{uuid}", campaignCustomerUuid, ExpresslyApiEndpoint.CONFIRM_MIGRATION));
        return client.call(new SuccessMessageResponse()).isSuccess();
    }

    @Override
    public BannerDetailResponse getCampaignBanner(String email) throws IOException {
        Builders.pattern(email, "email", "(.+@.+)");
        ExpresslyHttpClient client = expresslyClientFactory.makeClient(
                HttpGet.METHOD_NAME,
                buildEndpoint("{merchantUuid}", EncodingUtils.fromBase64(expresslyApiKey).split(":")[0], ExpresslyApiEndpoint.GET_BANNER));
        client.withQueryVariable("email", email);
        return client.call(new BannerDetailResponse());
    }

    private String buildEndpoint(String pathElement, String replacement, ExpresslyApiEndpoint endpointType) {
        return endpointType.getEndpoint()
                .replaceAll(Pattern.quote(pathElement), replacement);
    }
}
