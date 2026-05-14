package io.digitalid.ports.outputs;

import io.digitalid.domain.DigitalIdentityEntity;
import java.util.Optional;
import java.util.UUID;

public interface IdentityStorePort {
    void save(DigitalIdentityEntity identity);
    void delete(UUID digitalId);
    Optional<DigitalIdentityEntity> search(UUID digitalId); // can return id or null
}
