package io.digitalid.adapters.inbound.portals;

import io.digitalid.domain.*;
import io.digitalid.ports.inputs.IdentityInputPort;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.strategies.VerificationStrategy;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GovernmentPortalTest {

    private static final LocalDate DOB = LocalDate.of(2006, 6, 15);

    private IdentityInputPort identityPort;
    private VerificationInputPort verificationPort;
    private GovernmentPortal portal;

    @Before
    public void setUp() {
        identityPort = mock(IdentityInputPort.class);
        verificationPort = mock(VerificationInputPort.class);
        portal = new GovernmentPortal(identityPort, verificationPort);
    }

    private IdentityAttributes attrs() {
        return new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB");
    }

    @Test
    public void registersNewIdentity() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(attrs());
        when(identityPort.createIdentity(attrs())).thenReturn(entity);

        DigitalIdentityEntity result = portal.registerIdentity(attrs());

        assertEquals(entity, result);
    }

    @Test
    public void verifiesIdentityWithStrategy() {
        UUID id = UUID.randomUUID();
        VerificationStrategy strategy = mock(VerificationStrategy.class);
        VerificationResponse response = new VerificationResponse(id, true, LocalDateTime.now(), "Passed");
        when(verificationPort.verify(id, strategy)).thenReturn(response);

        assertTrue(portal.verifyIdentity(id, strategy).verified());
    }
}
