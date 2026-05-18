package io.digitalid.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record VerificationResponse(
    UUID digitalId,
    boolean verified,
    LocalDateTime verifiedAt,
    String message
) {}
