package io.digitalid.domain;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class DigitalIdentityEntity {
    private final UUID digitalId;
    private final LocalDate dateOfBirth;
    private final String nationality;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final LocalDateTime dateCreated;
    private final LocalDateTime expiryDate;

    private String address;
    private UserStatus status;
    private LocalDateTime lastModified;


    public DigitalIdentityEntity(IdentityAttributes request) {
        this.digitalId = UUID.randomUUID();
        this.dateCreated = LocalDateTime.now();
        this.expiryDate = this.dateCreated.plusYears(5);
        this.firstName = request.firstName();
        this.lastName = request.lastName();
        this.email = request.email();
        this.address = request.address();
        this.dateOfBirth = request.dateOfBirth();
        this.nationality = request.nationality();
        this.status = UserStatus.PENDING;
    }

    public UUID getDigitalId() { return digitalId; }
    public LocalDateTime getDateCreated() { return dateCreated; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getNationality() { return nationality; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public UserStatus getStatus() { return status; }

    public void setStatus(UserStatus status) {
        if (this.status.isRevoked()) {
            throw new IllegalStateException("[STATE ERROR] You cannot update a revoked identity");
        }
        this.status = status;
    }
    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
}
