package io.digitalid.services;

import io.digitalid.domain.AttributeValidator;
import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.domain.EventEntry;
import io.digitalid.domain.IdentityAttributes;
import io.digitalid.domain.UserStatus;
import io.digitalid.ports.inputs.IdentityInputPort;
import io.digitalid.ports.outputs.EventLogPort;
import io.digitalid.ports.outputs.IdentityStorePort;

import java.time.LocalDateTime;
import java.util.UUID;


public class IdentityService implements IdentityInputPort {

    private final IdentityStorePort store;
    private final EventLogPort eventLog;
    private final AttributeValidator validator;

    public IdentityService(IdentityStorePort store, EventLogPort eventLog, AttributeValidator validator) {
        this.store = store;
        this.eventLog = eventLog;
        this.validator = validator;
    }

    @Override
    public DigitalIdentityEntity createIdentity(IdentityAttributes attributes) {
        validator.validate(attributes);

        DigitalIdentityEntity identity = new DigitalIdentityEntity(attributes);
        store.save(identity);

        eventLog.record(new EventEntry(
            identity.getDigitalId(),
            "user_created",
            "New digital identity created for " + attributes.firstName() + " " + attributes.lastName(),
            LocalDateTime.now()
        ));

        return identity;
    }

    @Override
    public DigitalIdentityEntity updateIdentity(UUID digitalId, IdentityAttributes attributes) {
        DigitalIdentityEntity identity = searchIdentity(digitalId);

        identity.setAddress(attributes.address());
        identity.setLastModified(LocalDateTime.now());
        store.save(identity);

        eventLog.record(new EventEntry(
            digitalId,
            "user_updated",
            "Digital identity updated for " + attributes.firstName() + " " + attributes.lastName(),
            LocalDateTime.now()
        ));

        return identity;
    }

    @Override
    public DigitalIdentityEntity updateStatus(UUID digitalId, UserStatus newStatus) {
        DigitalIdentityEntity identity = searchIdentity(digitalId);
        UserStatus previousStatus = identity.getStatus();

        identity.setStatus(newStatus);
        identity.setLastModified(LocalDateTime.now());

        store.save(identity);

        eventLog.record(new EventEntry(
            digitalId,
            "status_change",
            previousStatus + "has now changed to " + newStatus,
            LocalDateTime.now()
        ));

        return identity;
    }

    private DigitalIdentityEntity searchIdentity(UUID digitalId) {
        return store.search(digitalId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Failed to find identity: " + digitalId));
    }
}
