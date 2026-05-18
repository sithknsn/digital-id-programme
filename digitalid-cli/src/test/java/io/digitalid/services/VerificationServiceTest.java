package io.digitalid.services;

import io.digitalid.domain.*;
import io.digitalid.ports.outputs.EventLogPort;
import io.digitalid.ports.outputs.IdentityStorePort;
import io.digitalid.strategies.VerificationStrategy;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VerificationServiceTest {

    private IdentityStorePort store;
    private EventLogPort eventLog;
    private VerificationStrategy strategy;
    private VerificationService service;

    @Before
    public void setUp() {
        store = mock(IdentityStorePort.class);
        eventLog = mock(EventLogPort.class);
        strategy = mock(VerificationStrategy.class);
        service = new VerificationService(store, eventLog);
    }

    private static final LocalDate DOB = LocalDate.of(2006, 6, 15);

    private DigitalIdentityEntity pending() {
        return new DigitalIdentityEntity(
            new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB")
        );
    }

    @Test
    public void checkStatusReturnsCurrentStatus() {
        DigitalIdentityEntity entity = pending();
        when(store.search(entity.getDigitalId())).thenReturn(Optional.of(entity));

        assertEquals(UserStatus.PENDING, service.checkStatus(entity.getDigitalId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkStatusThrowsWhenNotFound() {
        when(store.search(any())).thenReturn(Optional.empty());
        service.checkStatus(UUID.randomUUID());
    }

    @Test
    public void verifyPassActivatesAndLogs() {
        DigitalIdentityEntity entity = pending();
        when(store.search(entity.getDigitalId())).thenReturn(Optional.of(entity));

        VerificationResponse pass = new VerificationResponse(entity.getDigitalId(), true, LocalDateTime.now(), "All checks passed");
        when(strategy.verify(entity)).thenReturn(pass);

        VerificationResponse result = service.verify(entity.getDigitalId(), strategy);

        assertTrue(result.verified());
        assertEquals(UserStatus.ACTIVE, entity.getStatus());
        verify(store).save(entity);
        verify(eventLog).record(argThat(e -> e.action().equals("verification_pass")));
    }

    @Test
    public void verifyFailDoesNotChangeStatus() {
        DigitalIdentityEntity entity = pending();
        when(store.search(entity.getDigitalId())).thenReturn(Optional.of(entity));

        VerificationResponse fail = new VerificationResponse(entity.getDigitalId(), false, LocalDateTime.now(), "Document mismatch");
        when(strategy.verify(entity)).thenReturn(fail);

        VerificationResponse result = service.verify(entity.getDigitalId(), strategy);

        assertFalse(result.verified());
        assertEquals(UserStatus.PENDING, entity.getStatus());
        verify(store, never()).save(any());
        verify(eventLog).record(argThat(e -> e.action().equals("verification_fail")));
    }
}
