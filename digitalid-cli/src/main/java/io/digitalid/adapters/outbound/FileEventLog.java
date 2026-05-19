package io.digitalid.adapters.outbound;

import io.digitalid.domain.EventEntry;
import io.digitalid.ports.outputs.EventLogPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileEventLog implements EventLogPort {

    private final Path logFile;
    private final List<EventEntry> entries = new ArrayList<>();

    public FileEventLog(String filePath) {
        this.logFile = Path.of(filePath);
    }

    @Override
    public void record(EventEntry entry) {
        entries.add(entry);
        
        String line = String.format("[%s] %s | %s | %s%n",
            entry.timestamp(), entry.digitalId(), entry.action(), entry.details());
        try {
            Files.writeString(logFile, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to event log: " + logFile, e);
        }
    }

    @Override
    public List<EventEntry> retrieveEvents(UUID digitalId) {
        // Filter in-memory entries for the given identity — used by TaxAuthorityStrategy
        // to check historical status changes during a reporting period
        return entries.stream()
            .filter(e -> e.digitalId().equals(digitalId))
            .toList();
    }
}
