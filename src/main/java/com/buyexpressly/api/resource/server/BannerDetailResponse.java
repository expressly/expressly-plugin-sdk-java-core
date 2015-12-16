package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;


@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class BannerDetailResponse {
    private String bannerImageUrl;
    private String migrationLink;

    private BannerDetailResponse() {
    }

    public String getBannerImageUrl() {
        return this.bannerImageUrl;
    }

    public String getMigrationLink() {
        return this.migrationLink;
    }
}
