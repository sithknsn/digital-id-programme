package io.digitalid.adapters.outbound;

import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.domain.IdentityAttributes;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.*;

public class InMemoryIdentityStoreTest {

    private static final LocalDate DOB = LocalDate.of(2006, 6, 15);
    private final InMemoryIdentityStore store = new InMemoryIdentityStore();

    private DigitalIdentityEntity entity() {
        return new DigitalIdentityEntity(
            new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
    }

    @Test
    public void saveAndSearchReturnsEntity() {
        DigitalIdentityEntity entity = entity();
        store.save(entity);

        assertTrue(store.search(entity.getDigitalId()).isPresent());
    }

    @Test
    public void searchReturnsEmptyWhenNotFound() {
        assertTrue(store.search(UUID.randomUUID()).isEmpty());
    }

    @Test
    public void deleteRemovesEntity() {
        DigitalIdentityEntity entity = entity();
        store.save(entity);
        store.delete(entity.getDigitalId());

        assertTrue(store.search(entity.getDigitalId()).isEmpty());
    }
}
