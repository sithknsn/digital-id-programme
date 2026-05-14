package io.digitalid.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserStatusTest {

    @Test
    public void validTransitionsProduceCorrectState() {
        assertEquals(UserStatus.ACTIVE, UserStatus.UNVERIFIED.activate());
        assertEquals(UserStatus.SUSPENDED, UserStatus.ACTIVE.suspend());
        assertEquals(UserStatus.ACTIVE, UserStatus.SUSPENDED.activate());
        assertEquals(UserStatus.REVOKED, UserStatus.ACTIVE.revoke());
    }

    @Test(expected = IllegalStateException.class)
    public void revokedIdentityCannotTransition() {
        UserStatus.REVOKED.activate();
    }

    @Test(expected = IllegalStateException.class)
    public void invalidTransitionThrows() {
        UserStatus.UNVERIFIED.suspend();
    }
}
