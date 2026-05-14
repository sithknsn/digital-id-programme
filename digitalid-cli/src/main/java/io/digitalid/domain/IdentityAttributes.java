package io.digitalid.domain;

import java.time.LocalDate;

public record IdentityAttributes(
    String firstName,
    String lastName,
    String email,
    String address,
    LocalDate dateOfBirth,
    String nationality
) {}
