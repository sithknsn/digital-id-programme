package io.digitalid.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventEntry(
    UUID digitalId,
    String action,
    String details,
    LocalDateTime timestamp
) {}
