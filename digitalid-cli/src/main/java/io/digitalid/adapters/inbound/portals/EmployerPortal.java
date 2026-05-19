package io.digitalid.adapters.inbound.portals;

import io.digitalid.domain.VerificationResponse;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.strategies.EmployerStrategy;

import java.util.UUID;

// Employer portal, only allowed to verify identities

public class EmployerPortal {

    private final VerificationInputPort verificationPort;

    public EmployerPortal(VerificationInputPort verificationPort) {
        this.verificationPort = verificationPort;
    }

    public VerificationResponse verifyIdentity(UUID digitalId) {
        return verificationPort.verify(digitalId, new EmployerStrategy());
    }
}
