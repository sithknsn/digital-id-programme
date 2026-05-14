package io.digitalid.ports.inputs;

import java.util.UUID;

import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.domain.IdentityAttributes;
import io.digitalid.domain.UserStatus;

public interface IdentityInputPort {
    DigitalIdentityEntity createIdentity(IdentityAttributes attributes);
    DigitalIdentityEntity updateIdentity(UUID digitalId, IdentityAttributes attributes);
    DigitalIdentityEntity updateStatus(UUID digitalId, UserStatus status);
}
