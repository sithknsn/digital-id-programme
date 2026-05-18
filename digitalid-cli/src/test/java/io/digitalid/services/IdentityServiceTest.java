package io.digitalid.services;

import io.digitalid.domain.*;
import io.digitalid.ports.outputs.EventLogPort;
import io.digitalid.ports.outputs.IdentityStorePort;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IdentityServiceTest {

    private IdentityStorePort store;
    private EventLogPort eventLog;
    private AttributeValidator validator;
    private IdentityService service;

    @Before
    public void setUp() {
        store = mock(IdentityStorePort.class);
        eventLog = mock(EventLogPort.class);
        validator = new AttributeValidator();
        service = new IdentityService(store, eventLog, validator);
    }

    private static final LocalDate DOB = LocalDate.of(2006, 6, 15);

    private IdentityAttributes validAttrs() {
        return new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB");
    }

    @Test
    public void createIdentitySavesAndLogs() {
        DigitalIdentityEntity result = service.createIdentity(validAttrs());

        assertNotNull(result.getDigitalId());
        assertEquals(UserStatus.PENDING, result.getStatus());
        verify(store).save(result);
        verify(eventLog).record(argThat(e -> e.action().equals("user_created")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createIdentityRejectsInvalidAttributes() {
        service.createIdentity(new IdentityAttributes(null, "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
    }

    @Test
    public void updateIdentityChangesAddressAndLogs() {
        DigitalIdentityEntity existing = new DigitalIdentityEntity(validAttrs());
        when(store.search(existing.getDigitalId())).thenReturn(Optional.of(existing));

        IdentityAttributes updated = new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "67 Stonecot Avenue", DOB, "GB");
        DigitalIdentityEntity result = service.updateIdentity(existing.getDigitalId(), updated);

        assertEquals("67 Stonecot Avenue", result.getAddress());
        assertNotNull(result.getLastModified());
        verify(store, times(1)).save(existing);
        verify(eventLog).record(argThat(e -> e.action().equals("user_updated")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateIdentityThrowsWhenNotFound() {
        when(store.search(any())).thenReturn(Optional.empty());
        service.updateIdentity(UUID.randomUUID(), validAttrs());
    }

    @Test
    public void updateStatusChangesAndLogs() {
        DigitalIdentityEntity existing = new DigitalIdentityEntity(validAttrs());
        when(store.search(existing.getDigitalId())).thenReturn(Optional.of(existing));

        DigitalIdentityEntity result = service.updateStatus(existing.getDigitalId(), UserStatus.ACTIVE);

        assertEquals(UserStatus.ACTIVE, result.getStatus());
        verify(store).save(existing);
        verify(eventLog).record(argThat(e -> e.action().equals("status_change")));
    }
}
