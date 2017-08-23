package com.buyexpressly.api.providers;

import com.buyexpressly.api.resource.error.ExpresslyException;
import com.buyexpressly.api.resource.server.ExpresslyQuery;
import com.buyexpressly.api.util.Builders;
import com.buyexpressly.api.util.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

class ExpresslyHttpClient {
    private static final PoolingHttpClientConnectionManager CONNECTION_MANAGER = new PoolingHttpClientConnectionManager();
    private static final int MIN_ERROR_STATUS_CODE = 400;
    private static final int MAX_TOTAL_CONN = 200;
    private static final int MAX_CONN_PER_ROUTE = 20;
    private static final int CONN_TIMEOUT = 20;
    private RequestBuilder requestBuilder;
    private CloseableHttpClient httpClient;

    static {
        CONNECTION_MANAGER.setMaxTotal(MAX_TOTAL_CONN);
        CONNECTION_MANAGER.setDefaultMaxPerRoute(MAX_CONN_PER_ROUTE);
        CONNECTION_MANAGER.setValidateAfterInactivity(CONN_TIMEOUT);
    }

    protected ExpresslyHttpClient(String method, String uri, String expresslyApiKey, String expresslyEndpoint) {
        requestBuilder = RequestBuilder.create(method)
                .addHeader("Authorization", String.format("Basic %s", expresslyApiKey))
                .setUri(String.format("%s/%s", expresslyEndpoint, uri));
        httpClient = HttpClients.createMinimal(CONNECTION_MANAGER);
    }

    public void withQueryVariable(String name, String value) {
        if (!Builders.isNullOrEmpty(name) && !Builders.isNullOrEmpty(value)) {
            this.requestBuilder = requestBuilder.addParameters(new BasicNameValuePair(name, value));
        }
    }

    public <Q extends ExpresslyQuery> void withRequestBody(Q query) {
        this.requestBuilder = requestBuilder.setEntity(query);
    }

    public void withHttpClient(CloseableHttpClient client) {
        this.httpClient = client;
    }

    public <R> R call(final Class<R> responseType) throws IOException {
        HttpUriRequest httpRequest = requestBuilder.build();
        ResponseHandler<R> responseHandler = buildResponseHandler(responseType);
        return httpClient.execute(httpRequest, responseHandler);
    }

    private <R> ResponseHandler<R> buildResponseHandler(final Class<R> responseType) {
        return new ResponseHandler<R>() {
            @Override
            public R handleResponse(final HttpResponse response) throws IOException, ExpresslyException {
                if (response.getStatusLine().getStatusCode() >= MIN_ERROR_STATUS_CODE) {
                    throw new ExpresslyException(String.format("Error occurred in request to Expressly Server. StatusCode=%s, Detail=%s ResponseContent=%s",
                            response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), EntityUtils.toString(response.getEntity())));
                }
                return handleEntity(response.getEntity(), String.valueOf(response.getStatusLine().getStatusCode()));
            }

            private R handleEntity(HttpEntity entity, String status) throws IOException {
                switch (ContentType.getOrDefault(entity).getMimeType()) {
                    case "text/html":
                        return (R) EntityUtils.toString(entity);
                    case "application/json":
                        ObjectMapper om = ObjectMapperFactory.make();
                        return om.readValue(EntityUtils.toString(entity), responseType);
                    default:
                        return (R) status;
                }
            }
        };
    }
}
