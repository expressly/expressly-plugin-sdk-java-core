package com.buyexpressly.api;

import com.buyexpressly.api.resource.server.BannerDetailResponse;
import com.buyexpressly.api.resource.server.MigrationResponse;

import java.io.IOException;

public interface ExpresslyProvider {
    ///////////////////////////////////////////////////////////////////////////
    // Plugin lifecycle
    ///////////////////////////////////////////////////////////////////////////

    boolean ping() throws IOException;

    boolean install(String apiBaseUrl) throws IOException;

    boolean uninstall() throws IOException;

    ///////////////////////////////////////////////////////////////////////////
    // Customer migration
    ///////////////////////////////////////////////////////////////////////////

    String fetchMigrationConfirmationHtml(String campaignCustomerUuid) throws IOException;

    MigrationResponse fetchMigrationCustomerData(String campaignCustomerUuid) throws IOException;

    boolean finaliseMigrationOfCustomerData(String campaignCustomerUuid) throws IOException;

    ///////////////////////////////////////////////////////////////////////////
    // Banner
    ///////////////////////////////////////////////////////////////////////////

    BannerDetailResponse getCampaignBanner(String email) throws IOException;

    ///////////////////////////////////////////////////////////////////////////
}
