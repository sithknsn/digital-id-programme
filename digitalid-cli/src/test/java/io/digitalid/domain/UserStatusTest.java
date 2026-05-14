package io.digitalid.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserStatusTest {

    @Test
    public void validTransitionsProduceCorrectState() {
        assertEquals(UserStatus.ACTIVE, UserStatus.PENDING.activate());
        assertEquals(UserStatus.INACTIVE, UserStatus.ACTIVE.deactivate());
        assertEquals(UserStatus.ACTIVE, UserStatus.INACTIVE.activate());
        assertEquals(UserStatus.REVOKED, UserStatus.ACTIVE.revoke());
    }

    @Test(expected = IllegalStateException.class)
    public void revokedIdentityCannotTransition() {
        UserStatus.REVOKED.activate();
    }

    @Test(expected = IllegalStateException.class)
    public void invalidTransitionThrows() {
        UserStatus.PENDING.deactivate();
    }
}
