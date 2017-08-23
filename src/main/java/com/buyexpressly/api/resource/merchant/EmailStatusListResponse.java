package com.buyexpressly.api.resource.merchant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        private List<String> existing = new ArrayList<>();
        private List<String> pending = new ArrayList<>();
        private List<String> deleted = new ArrayList<>();

        public Builder addExisting(List<String> existing) {
            this.existing.addAll(existing == null ? new ArrayList<>() : existing);
            return this;
        }

        public Builder addPending(List<String> pending) {
            this.pending.addAll(pending == null ? new ArrayList<>() : pending);
            return this;
        }

        public Builder addDeleted(List<String> deleted) {
            this.deleted.addAll(deleted == null ? new ArrayList<>() : deleted);
            return this;
        }

        public EmailStatusListResponse build() {
            return new EmailStatusListResponse(this);
        }

    }

}
