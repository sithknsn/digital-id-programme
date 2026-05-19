package io.digitalid.adapters.outbound;

import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.ports.outputs.IdentityStorePort;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// serialises the identity map to disk on every write, and loads it on startup
public class FileIdentityStore implements IdentityStorePort {

    private final Path storeFile;
    private final Map<UUID, DigitalIdentityEntity> store;

    public FileIdentityStore(String filePath) {
        this.storeFile = Path.of(filePath);
        this.store = loadFromDisk();
    }

    @Override
    public void save(DigitalIdentityEntity identity) {
        store.put(identity.getDigitalId(), identity);
        writeToDisk();
    }

    @Override
    public void delete(UUID digitalId) {
        store.remove(digitalId);
        writeToDisk();
    }

    @Override
    public Optional<DigitalIdentityEntity> search(UUID digitalId) {
        return Optional.ofNullable(store.get(digitalId));
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, DigitalIdentityEntity> loadFromDisk() {
        if (!Files.exists(storeFile)) {
            return new HashMap<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(storeFile.toFile()))) {
            return (Map<UUID, DigitalIdentityEntity>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("  [!] Could not load identity store. Starting fresh.");
            return new HashMap<>();
        }
    }

    private void writeToDisk() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(storeFile.toFile()))) {
            out.writeObject(store);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist identity store: " + storeFile, e);
        }
    }
}
