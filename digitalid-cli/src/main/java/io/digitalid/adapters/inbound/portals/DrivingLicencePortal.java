package io.digitalid.adapters.inbound.portals;

import io.digitalid.domain.VerificationResponse;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.strategies.DrivingLicenceStrategy;

import java.util.UUID;


 // Driving license authority portal can only verify identities, checks identity is active and holder meets the minimum age

public class DrivingLicencePortal {

    private static final int UK_DRIVING_AGE = 17;

    private final VerificationInputPort verificationPort;

    public DrivingLicencePortal(VerificationInputPort verificationPort) {
        this.verificationPort = verificationPort;
    }

    public VerificationResponse verifyEligibility(UUID digitalId) {
        var strategy = new DrivingLicenceStrategy(UK_DRIVING_AGE);
        return verificationPort.verify(digitalId, strategy);
    }

    public VerificationResponse verifyEligibility(UUID digitalId, int minimumAge) {
        var strategy = new DrivingLicenceStrategy(minimumAge);
        return verificationPort.verify(digitalId, strategy);
    }
}
