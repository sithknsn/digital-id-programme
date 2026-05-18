package io.digitalid.strategies;

import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.domain.UserStatus;
import io.digitalid.domain.VerificationResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class DrivingLicenceStrategy implements VerificationStrategy {

    private final int minimumAge;

    public DrivingLicenceStrategy(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    @Override
    public VerificationResponse verify(DigitalIdentityEntity identity) {
        if (identity.getStatus() != UserStatus.ACTIVE) {
            return new VerificationResponse(
                identity.getDigitalId(),
                false,
                LocalDateTime.now(),
                "Identity is not currently active"
            );
        }

        int age = Period.between(identity.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < minimumAge) {
            return new VerificationResponse(
                identity.getDigitalId(),
                false,
                LocalDateTime.now(),
                "Does not meet minimum age requirement (" + minimumAge + ")"
            );
        }

        return new VerificationResponse(
            identity.getDigitalId(),
            true,
            LocalDateTime.now(),
            "Identity active and age requirement met"
        );
    }
}
