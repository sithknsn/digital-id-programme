package io.digitalid.domain;

public enum UserStatus {
    PENDING {
        @Override
        public UserStatus activate() { return ACTIVE; }
    },
    ACTIVE {
        @Override
        public UserStatus deactivate() { return INACTIVE; }

        @Override
        public UserStatus expire() { return EXPIRED; }

        @Override
        public UserStatus revoke() { return REVOKED; }
    },
    INACTIVE {
        @Override
        public UserStatus activate() { return ACTIVE; }

        @Override
        public UserStatus expire() { return EXPIRED; }

        @Override
        public UserStatus revoke() { return REVOKED; }
    },
    EXPIRED {
        @Override
        public UserStatus activate() { return ACTIVE; }

        @Override
        public UserStatus revoke() { return REVOKED; }
    },
    REVOKED;

    public UserStatus activate() {
        throw new IllegalStateException("[STATE ERROR] You cannot activate from " + this);
    }

    public UserStatus deactivate() {
        throw new IllegalStateException("[STATE ERROR] You cannot deactivate from " + this);
    }

    public UserStatus expire() {
        throw new IllegalStateException("[STATE ERROR] You cannot expire from " + this);
    }

    public UserStatus revoke() {
        throw new IllegalStateException("[STATE ERROR] You cannot revoke from " + this);
    }

    public boolean isRevoked() {
        return this == REVOKED;
    }
}
