package io.digitalid.strategies;

import io.digitalid.domain.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class DrivingLicenceStrategyTest {

    private static final LocalDate DOB = LocalDate.of(2006, 6, 15);

    private DigitalIdentityEntity activeIdentity() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
            new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
        entity.setStatus(UserStatus.ACTIVE);
        return entity;
    }

    @Test
    public void passesWhenActiveAndMeetsAge() {
        DrivingLicenceStrategy strategy = new DrivingLicenceStrategy(17);
        VerificationResponse response = strategy.verify(activeIdentity());

        assertTrue(response.verified());
    }

    @Test
    public void failsWhenBelowMinimumAge() {
        DrivingLicenceStrategy strategy = new DrivingLicenceStrategy(25);
        VerificationResponse response = strategy.verify(activeIdentity());

        assertFalse(response.verified());
        assertTrue(response.message().contains("25"));
    }

    @Test
    public void failsWhenNotActive() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
            new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));

        DrivingLicenceStrategy strategy = new DrivingLicenceStrategy(17);
        VerificationResponse response = strategy.verify(entity);

        assertFalse(response.verified());
        assertTrue(response.message().contains("not currently active"));
    }
}
