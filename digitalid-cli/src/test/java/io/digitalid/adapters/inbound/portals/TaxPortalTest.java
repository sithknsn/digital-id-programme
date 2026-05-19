package io.digitalid.adapters.inbound.portals;

import io.digitalid.domain.VerificationResponse;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.ports.outputs.EventLogPort;
import io.digitalid.strategies.TaxAuthorityStrategy;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TaxPortalTest {

    @Test
    public void verifiesIdentityForTaxPeriod() {
        VerificationInputPort port = mock(VerificationInputPort.class);
        EventLogPort eventLog = mock(EventLogPort.class);
        TaxPortal portal = new TaxPortal(port, eventLog);

        UUID id = UUID.randomUUID();
        VerificationResponse response = new VerificationResponse(id, true, LocalDateTime.now(), "Active throughout");
        when(port.verify(eq(id), any(TaxAuthorityStrategy.class))).thenReturn(response);

        assertTrue(portal.verifyForPeriod(id, LocalDate.of(2025, 4, 6), LocalDate.of(2026, 4, 5)).verified());
    }
}
