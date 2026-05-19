package io.digitalid.adapters.inbound.portals;

import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.domain.IdentityAttributes;
import io.digitalid.domain.UserStatus;
import io.digitalid.domain.VerificationResponse;
import io.digitalid.ports.inputs.IdentityInputPort;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.strategies.VerificationStrategy;

import java.util.UUID;

public class GovernmentPortal {
    
    private final IdentityInputPort identityPort;
    private final VerificationInputPort verificationPort;

    public GovernmentPortal(IdentityInputPort identityPort, VerificationInputPort verificationPort) {
        this.identityPort = identityPort;
        this.verificationPort = verificationPort;
    }

    public DigitalIdentityEntity registerIdentity(IdentityAttributes attributes) {
        return identityPort.createIdentity(attributes);
    }

    public DigitalIdentityEntity updateDetails(UUID digitalId, IdentityAttributes attributes) {
        return identityPort.updateIdentity(digitalId, attributes);
    }

    public DigitalIdentityEntity changeStatus(UUID digitalId, UserStatus status) {
        return identityPort.updateStatus(digitalId, status);
    }

    public UserStatus lookupStatus(UUID digitalId) {
        return verificationPort.checkStatus(digitalId);
    }

    public VerificationResponse verifyIdentity(UUID digitalId, VerificationStrategy strategy) {
        return verificationPort.verify(digitalId, strategy);
    }
}
