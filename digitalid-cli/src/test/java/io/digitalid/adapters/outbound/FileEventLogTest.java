package io.digitalid.adapters.outbound;

import io.digitalid.domain.EventEntry;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;

public class FileEventLogTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void recordWritesToFileAndRetrievesById() throws IOException {
        Path logFile = tempFolder.newFile("events.log").toPath();
        FileEventLog eventLog = new FileEventLog(logFile.toString());

        UUID id = UUID.randomUUID();
        eventLog.record(new EventEntry(id, "user_created", "New digital identity created for Jane Doe", LocalDateTime.of(2026, 1, 15, 10, 0)));
        eventLog.record(new EventEntry(UUID.randomUUID(), "user_created", "New digital identity created for John Doe", LocalDateTime.of(2026, 1, 15, 10, 5)));

        String content = Files.readString(logFile);
        assertTrue(content.contains("user_created"));

        assertEquals(1, eventLog.retrieveEvents(id).size());
    }
}
