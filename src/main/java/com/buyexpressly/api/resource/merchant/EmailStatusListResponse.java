package com.buyexpressly.api.resource.merchant;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.util.Collections;
import java.util.List;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class EmailStatusListResponse {
    private final List<String> existing;
    private final List<String> pending;
    private final List<String> deleted;

    private EmailStatusListResponse(Builder builder) {
        this.existing = Collections.unmodifiableList(builder.existing);
        this.pending = Collections.unmodifiableList(builder.pending);
        this.deleted = Collections.unmodifiableList(builder.deleted);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<String> existing;
        private List<String> pending;
        private List<String> deleted;

        public Builder addExisting(List<String> existing) {
            this.existing = existing;
            return this;
        }

        public Builder addPending(List<String> pending) {
            this.pending = pending;
            return this;
        }

        public Builder addDeleted(List<String> deleted) {
            this.deleted = deleted;
            return this;
        }

        public EmailStatusListResponse build() {
            return new EmailStatusListResponse(this);
        }

    }

}
