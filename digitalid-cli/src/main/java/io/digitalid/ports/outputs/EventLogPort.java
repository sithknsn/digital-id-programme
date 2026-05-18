package io.digitalid.ports.outputs;

import io.digitalid.domain.EventEntry;
import java.util.List;
import java.util.UUID;

public interface EventLogPort {
    void record(EventEntry entry);
    List<EventEntry> retrieveEvents(UUID digitalId);
}
