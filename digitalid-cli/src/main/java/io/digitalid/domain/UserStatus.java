package io.digitalid.domain;

public enum UserStatus {
    UNVERIFIED {
        @Override
        public UserStatus activate() { return ACTIVE; }
    },
    ACTIVE {
        @Override
        public UserStatus suspend() { return SUSPENDED; }

        @Override
        public UserStatus expire() { return EXPIRED; }

        @Override
        public UserStatus revoke() { return REVOKED; }
    },
    SUSPENDED {
        @Override
        public UserStatus activate() { return ACTIVE; }

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

    public UserStatus suspend() {
        throw new IllegalStateException("[STATE ERROR] You cannot suspend from " + this);
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
