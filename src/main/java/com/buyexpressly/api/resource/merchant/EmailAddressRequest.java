package com.buyexpressly.api.resource.merchant;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.util.List;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class EmailAddressRequest {
    private List<String> emails;

    private EmailAddressRequest() {

    }

    public List<String> getEmails() {
        return emails;
    }

}
