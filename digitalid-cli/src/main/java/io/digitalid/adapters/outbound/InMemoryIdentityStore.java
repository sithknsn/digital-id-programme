package io.digitalid.adapters.outbound;

import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.ports.outputs.IdentityStorePort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class InMemoryIdentityStore implements IdentityStorePort {
    private final Map<UUID, DigitalIdentityEntity> store = new HashMap<>();

    @Override
    public void save(DigitalIdentityEntity identity) {
        // put() handles both insert (new) and update (existing) 
        store.put(identity.getDigitalId(), identity);
    }

    @Override
    public void delete(UUID digitalId) {
        store.remove(digitalId);
    }

    @Override
    public Optional<DigitalIdentityEntity> search(UUID digitalId) {
        return Optional.ofNullable(store.get(digitalId));
    }
}
