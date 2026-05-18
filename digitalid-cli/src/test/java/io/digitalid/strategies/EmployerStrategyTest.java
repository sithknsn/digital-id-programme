package io.digitalid.strategies;

import io.digitalid.domain.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class EmployerStrategyTest {

    private static final LocalDate DOB = LocalDate.of(2006, 6, 15);
    private final EmployerStrategy strategy = new EmployerStrategy();

    @Test
    public void passesWhenActive() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
            new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
        entity.setStatus(UserStatus.ACTIVE);

        VerificationResponse response = strategy.verify(entity);

        assertTrue(response.verified());
        assertEquals("Identity is valid", response.message());
    }

    @Test
    public void failsWhenPending() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
            new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));

        VerificationResponse response = strategy.verify(entity);

        assertFalse(response.verified());
        assertEquals("Identity is not valid", response.message());
    }

    @Test
    public void failsWhenExpired() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
            new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
        entity.setStatus(UserStatus.ACTIVE);
        entity.setStatus(UserStatus.EXPIRED);

        VerificationResponse response = strategy.verify(entity);

        assertFalse(response.verified());
    }
}
