package io.digitalid.strategies;

import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.domain.UserStatus;
import io.digitalid.domain.VerificationResponse;

import java.time.LocalDateTime;

public class EmployerStrategy implements VerificationStrategy {

    @Override
    public VerificationResponse verify(DigitalIdentityEntity identity) {
        boolean isActive = identity.getStatus() == UserStatus.ACTIVE;

        return new VerificationResponse(
            identity.getDigitalId(),
            isActive,
            LocalDateTime.now(),
            isActive ? "Identity is valid" : "Identity is not valid"
        );
    }
}
