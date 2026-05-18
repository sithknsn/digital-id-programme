package io.digitalid.strategies;

import io.digitalid.domain.*;
import io.digitalid.ports.outputs.EventLogPort;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TaxAuthorityStrategyTest {

    private static final LocalDate DOB = LocalDate.of(2006, 6, 15);
    private static final LocalDate PERIOD_START = LocalDate.of(2025, 4, 6);
    private static final LocalDate PERIOD_END = LocalDate.of(2026, 4, 5);

    private EventLogPort eventLog;

    @Before
    public void setUp() {
        eventLog = mock(EventLogPort.class);
    }

    private DigitalIdentityEntity activeIdentity() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
            new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
        entity.setStatus(UserStatus.ACTIVE);
        return entity;
    }

    @Test
    public void passesWhenActiveWithNoStatusChanges() {
        DigitalIdentityEntity entity = activeIdentity();
        when(eventLog.retrieveEvents(entity.getDigitalId())).thenReturn(List.of());

        TaxAuthorityStrategy strategy = new TaxAuthorityStrategy(PERIOD_START, PERIOD_END, eventLog);
        VerificationResponse response = strategy.verify(entity);

        assertTrue(response.verified());
        assertTrue(response.message().contains("active throughout"));
    }

    @Test
    public void failsWhenNotActive() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
            new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));

        TaxAuthorityStrategy strategy = new TaxAuthorityStrategy(PERIOD_START, PERIOD_END, eventLog);
        VerificationResponse response = strategy.verify(entity);

        assertFalse(response.verified());
        assertTrue(response.message().contains("not currently active"));
    }

    @Test
    public void failsWhenInactiveDuringPeriod() {
        DigitalIdentityEntity entity = activeIdentity();
        UUID id = entity.getDigitalId();

        EventEntry inactiveEvent = new EventEntry(
            id, "status_change", "ACTIVE has now changed to INACTIVE",
            LocalDateTime.of(2025, 8, 1, 10, 0));

        when(eventLog.retrieveEvents(id)).thenReturn(List.of(inactiveEvent));

        TaxAuthorityStrategy strategy = new TaxAuthorityStrategy(PERIOD_START, PERIOD_END, eventLog);
        VerificationResponse response = strategy.verify(entity);

        assertFalse(response.verified());
        assertTrue(response.message().contains("inactive during reporting period"));
    }

    @Test
    public void passesWhenInactiveOutsidePeriod() {
        DigitalIdentityEntity entity = activeIdentity();
        UUID id = entity.getDigitalId();

        EventEntry oldEvent = new EventEntry(
            id, "status_change", "ACTIVE has now changed to INACTIVE",
            LocalDateTime.of(2024, 1, 1, 10, 0));

        when(eventLog.retrieveEvents(id)).thenReturn(List.of(oldEvent));

        TaxAuthorityStrategy strategy = new TaxAuthorityStrategy(PERIOD_START, PERIOD_END, eventLog);
        VerificationResponse response = strategy.verify(entity);

        assertTrue(response.verified());
    }

    @Test
    public void failsWhenExpiredDuringPeriod() {
        DigitalIdentityEntity entity = activeIdentity();
        UUID id = entity.getDigitalId();

        EventEntry expiredEvent = new EventEntry(
            id, "status_change", "ACTIVE has now changed to EXPIRED",
            LocalDateTime.of(2025, 12, 25, 9, 0));

        when(eventLog.retrieveEvents(id)).thenReturn(List.of(expiredEvent));

        TaxAuthorityStrategy strategy = new TaxAuthorityStrategy(PERIOD_START, PERIOD_END, eventLog);
        VerificationResponse response = strategy.verify(entity);

        assertFalse(response.verified());
    }
}
