package io.digitalid.adapters.inbound.portals;

import io.digitalid.domain.VerificationResponse;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.ports.outputs.EventLogPort;
import io.digitalid.strategies.TaxAuthorityStrategy;

import java.time.LocalDate;
import java.util.UUID;

// Tax authority, can only verify identities for a period

public class TaxPortal {

    private final VerificationInputPort verificationPort;
    private final EventLogPort eventLog;

    public TaxPortal(VerificationInputPort verificationPort, EventLogPort eventLog) {
        this.verificationPort = verificationPort;
        this.eventLog = eventLog;
    }

    public VerificationResponse verifyForPeriod(UUID digitalId, LocalDate periodStart, LocalDate periodEnd) {
        var strategy = new TaxAuthorityStrategy(periodStart, periodEnd, eventLog);
        return verificationPort.verify(digitalId, strategy);
    }
}
