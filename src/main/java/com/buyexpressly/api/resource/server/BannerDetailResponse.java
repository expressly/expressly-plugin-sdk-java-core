package com.buyexpressly.api.resource.server;

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
