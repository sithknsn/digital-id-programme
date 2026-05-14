package io.digitalid.domain;

import org.junit.Test;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class DigitalIdentityEntityTest {

    @Test
    public void immutableFieldsPreservedAfterConstruction() {
        LocalDate dob = LocalDate.of(2006, 6, 15);
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
                new IdentityAttributes("Jane", "Doe", "jane@gmail.com", "66 Stonecot Avenue", dob, "GB"));
        assertEquals("Jane", entity.getFirstName());
        assertEquals(dob, entity.getDateOfBirth());
        assertEquals("GB", entity.getNationality());
    }

    @Test
    public void mutableFieldsCanBeUpdated() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
                new IdentityAttributes("John", "Doe", "john@gmail.com", "66 Stonecot Avenue",
                LocalDate.of(2006, 6, 15), "GB"));
        entity.setAddress("67 Stonecot Avenue");
        assertEquals("67 Stonecot Avenue", entity.getAddress());
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpdateRevokedEntity() {
        DigitalIdentityEntity entity = new DigitalIdentityEntity(
                new IdentityAttributes("John", "Doe", "john@gmail.com", "66 Stonecot Avenue",
                LocalDate.of(1990, 1, 1), "GB"));
        entity.setStatus(UserStatus.ACTIVE);
        entity.setStatus(UserStatus.REVOKED);
        entity.setStatus(UserStatus.ACTIVE);
    }
}
