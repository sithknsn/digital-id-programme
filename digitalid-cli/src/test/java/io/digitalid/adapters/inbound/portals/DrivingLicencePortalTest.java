package io.digitalid.adapters.inbound.portals;

import io.digitalid.domain.VerificationResponse;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.strategies.DrivingLicenceStrategy;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DrivingLicencePortalTest {

    @Test
    public void verifiesDriverEligibility() {
        VerificationInputPort port = mock(VerificationInputPort.class);
        DrivingLicencePortal portal = new DrivingLicencePortal(port);

        UUID id = UUID.randomUUID();
        VerificationResponse response = new VerificationResponse(id, true, LocalDateTime.now(), "Passed");
        when(port.verify(eq(id), any(DrivingLicenceStrategy.class))).thenReturn(response);

        assertTrue(portal.verifyEligibility(id).verified());
    }
}
