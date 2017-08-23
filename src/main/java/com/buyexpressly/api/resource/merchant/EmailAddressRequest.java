package com.buyexpressly.api.resource.merchant;

import java.util.List;

public final class EmailAddressRequest {
    private List<String> emails;

    private EmailAddressRequest() {
    }

    public List<String> getEmails() {
        return emails;
    }

}
