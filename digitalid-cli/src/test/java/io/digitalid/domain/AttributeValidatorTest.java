package io.digitalid.domain;

import org.junit.Test;

import java.time.LocalDate;

public class AttributeValidatorTest {

    private final AttributeValidator validator = new AttributeValidator();

    private static final LocalDate DOB = LocalDate.of(2006, 6, 15);

    private IdentityAttributes valid() {
        return new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB");
    }

    @Test
    public void acceptsValidAttributes() {
        validator.validate(valid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNullFirstName() {
        validator.validate(new IdentityAttributes(null, "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsBlankFirstName() {
        validator.validate(new IdentityAttributes("  ", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNullLastName() {
        validator.validate(new IdentityAttributes("Jane", null, "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsBlankLastName() {
        validator.validate(new IdentityAttributes("Jane", "  ", "jane@gmail.com", "66 Stonecot Avenue", DOB, "GB"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNullDateOfBirth() {
        validator.validate(new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", null, "GB"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNullNationality() {
        validator.validate(new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsBlankNationality() {
        validator.validate(new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", DOB, ""));
    }
}
