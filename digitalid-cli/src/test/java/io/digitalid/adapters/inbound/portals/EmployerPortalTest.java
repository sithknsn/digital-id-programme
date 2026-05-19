package io.digitalid.adapters.inbound.portals;

import io.digitalid.domain.VerificationResponse;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.strategies.EmployerStrategy;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EmployerPortalTest {

    @Test
    public void verifiesEmployeeIdentity() {
        VerificationInputPort port = mock(VerificationInputPort.class);
        EmployerPortal portal = new EmployerPortal(port);

        UUID id = UUID.randomUUID();
        VerificationResponse response = new VerificationResponse(id, true, LocalDateTime.now(), "Identity is valid");
        when(port.verify(eq(id), any(EmployerStrategy.class))).thenReturn(response);

        assertTrue(portal.verifyIdentity(id).verified());
    }
}
