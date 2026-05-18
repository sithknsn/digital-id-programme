package io.digitalid.strategies;
import io.digitalid.domain.VerificationResponse;
import io.digitalid.domain.DigitalIdentityEntity;

public interface VerificationStrategy {
      VerificationResponse verify(DigitalIdentityEntity identity);
  }