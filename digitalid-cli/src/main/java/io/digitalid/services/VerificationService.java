package io.digitalid.services;

import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.domain.EventEntry;
import io.digitalid.domain.UserStatus;
import io.digitalid.domain.VerificationResponse;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.ports.outputs.EventLogPort;
import io.digitalid.ports.outputs.IdentityStorePort;
import io.digitalid.strategies.VerificationStrategy;

import java.time.LocalDateTime;
import java.util.UUID;


public class VerificationService implements VerificationInputPort {

    private final IdentityStorePort store;
    private final EventLogPort eventLog;

    public VerificationService(IdentityStorePort store, EventLogPort eventLog) {
        this.store = store;
        this.eventLog = eventLog;
    }

    @Override
    public UserStatus checkStatus(UUID digitalId) {
        return searchIdentity(digitalId).getStatus();
    }

    @Override
    public VerificationResponse verify(UUID digitalId, VerificationStrategy strategy) {
        DigitalIdentityEntity identity = searchIdentity(digitalId);
        VerificationResponse response = strategy.verify(identity);

        // verified: stautus goes from PENDING to ACTIVE
        if (response.verified()) {
            identity.setStatus(UserStatus.ACTIVE);
            identity.setLastModified(LocalDateTime.now());
            store.save(identity);
        }

        eventLog.record(new EventEntry(
            digitalId,
            response.verified() ? "verification_pass" : "verification_fail",
            response.message(),
            LocalDateTime.now()
        ));

        return response;
    }

    private DigitalIdentityEntity searchIdentity(UUID digitalId) {
        return store.search(digitalId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Failed to find identity: " + digitalId));
    }
}
