package io.digitalid.domain;


public class AttributeValidator {

    public void validate(IdentityAttributes attributes) {
        if (attributes.firstName() == null || attributes.firstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (attributes.lastName() == null || attributes.lastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (attributes.dateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        if (attributes.nationality() == null || attributes.nationality().isBlank()) {
            throw new IllegalArgumentException("Nationality is required");
        }
    }
}
