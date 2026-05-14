package io.digitalid.domain;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class DigitalIdentityEntity {
    private final UUID digitalId;
    private final LocalDateTime dateCreated;
    private final LocalDate dateOfBirth;
    private final String nationality;
    private String firstName;
    private String lastName;
    private String email;
    private UserStatus status;
    private LocalDateTime lastModified;

    public DigitalIdentityEntity(LocalDate dateOfBirth, String nationality) {
        this.digitalId = UUID.randomUUID();
        this.dateCreated = LocalDateTime.now();
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.status = UserStatus.UNVERIFIED;
    }

    public UUID getDigitalId() { return digitalId; }
    public LocalDateTime getDateCreated() { return dateCreated; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getNationality() { return nationality; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public UserStatus getStatus() { return status; }

    protected void setStatus(UserStatus status) {
        if (this.status.isRevoked()){
            throw new IllegalStateException("[STATE ERROR] You cannot update a revoked identity");

        }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
}
