package io.digitalid.adapters.inbound.console;

import io.digitalid.adapters.inbound.portals.DrivingLicencePortal;
import io.digitalid.adapters.inbound.portals.EmployerPortal;
import io.digitalid.adapters.inbound.portals.GovernmentPortal;
import io.digitalid.adapters.inbound.portals.TaxPortal;
import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.domain.IdentityAttributes;
import io.digitalid.domain.UserStatus;
import io.digitalid.domain.VerificationResponse;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.UUID;


public class IdentityConsole {

    private final GovernmentPortal governmentPortal;
    private final TaxPortal taxPortal;
    private final DrivingLicencePortal drivingLicencePortal;
    private final EmployerPortal employerPortal;
    private final Scanner scanner;

    public IdentityConsole(
            GovernmentPortal governmentPortal,
            TaxPortal taxPortal,
            DrivingLicencePortal drivingLicencePortal,
            EmployerPortal employerPortal) {
        this.governmentPortal = governmentPortal;
        this.taxPortal = taxPortal;
        this.drivingLicencePortal = drivingLicencePortal;
        this.employerPortal = employerPortal;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println();
        System.out.println("  Digital Identity Programme [CLI]");
        System.out.println("  ===========================================");
        System.out.println();

        while (true) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleGovernmentPortal();
                case "2" -> handleTaxPortal();
                case "3" -> handleDrivingLicencePortal();
                case "4" -> handleEmployerPortal();
                case "0" -> {
                    System.out.println();
                    System.out.println("  Session ended.");
                    return;
                }
                default -> System.out.println("  [!] Unrecognised option. Enter a number from the menu.");
            }
            System.out.println();
        }
    }

    private void printMainMenu() {
        System.out.println("  Which portal are you accessing?");
        System.out.println();
        System.out.println("    1  Government (Central Authority)");
        System.out.println("    2  HMRC (Tax Authority)");
        System.out.println("    3  DVLA (Driving Licence)");
        System.out.println("    4  Employer / Bank");
        System.out.println("    0  Exit");
        System.out.println();
        System.out.print("  > ");
    }


    private void handleGovernmentPortal() {
        System.out.println();
        System.out.println("  === Government Portal ===");
        System.out.println();
        System.out.println("    1  Register a new citizen");
        System.out.println("    2  Amend identity details");
        System.out.println("    3  Transition status");
        System.out.println("    4  Query current status");
        System.out.println();
        System.out.print("  > ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> registerIdentity();
            case "2" -> updateIdentity();
            case "3" -> changeStatus();
            case "4" -> lookupStatus();
            default -> System.out.println("  [!] Unrecognised option.");
        }
    }

    private void registerIdentity() {
        System.out.println();
        System.out.println("  Registering new identity...");
        System.out.println();

        System.out.print("  first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("  last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("  email: ");
        String email = scanner.nextLine().trim();
        System.out.print("  address: ");
        String address = scanner.nextLine().trim();
        System.out.print("  date of birth (yyyy-mm-dd): ");
        LocalDate dob = parseDate(scanner.nextLine().trim());
        if (dob == null) return;
        System.out.print("  nationality: ");
        String nationality = scanner.nextLine().trim();

        System.out.println();
        System.out.println("  Processing...");

        try {
            var attributes = new IdentityAttributes(firstName, lastName, email, address, dob, nationality);
            DigitalIdentityEntity entity = governmentPortal.registerIdentity(attributes);
            System.out.println("  Done. Identity registered successfully.");
            System.out.println();
            printIdentity(entity);
        } catch (IllegalArgumentException e) {
            System.out.println("  [REJECTED] " + e.getMessage());
        }
    }

    private void updateIdentity() {
        System.out.println();
        System.out.println("  Amend an existing identity. Requires the digital ID.");
        UUID id = promptForId();
        if (id == null) return;

        System.out.println();
        System.out.println("  Enter updated details:");
        System.out.print("  first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("  last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("  email: ");
        String email = scanner.nextLine().trim();
        System.out.print("  address: ");
        String address = scanner.nextLine().trim();
        System.out.print("  date of birth (yyyy-mm-dd): ");
        LocalDate dob = parseDate(scanner.nextLine().trim());
        if (dob == null) return;
        System.out.print("  nationality: ");
        String nationality = scanner.nextLine().trim();

        System.out.println();
        System.out.println("  Applying changes...");

        try {
            var attributes = new IdentityAttributes(firstName, lastName, email, address, dob, nationality);
            DigitalIdentityEntity entity = governmentPortal.updateDetails(id, attributes);
            System.out.println("  Done. Record amended.");
            System.out.println();
            printIdentity(entity);
        } catch (IllegalArgumentException e) {
            System.out.println("  [REJECTED] " + e.getMessage());
        }
    }

    private void changeStatus() {
        System.out.println();
        System.out.println("  Transition an identity's status. Requires the digital ID.");
        UUID id = promptForId();
        if (id == null) return;

        System.out.println();
        System.out.println("  Target status:");
        System.out.println("    1  ACTIVE");
        System.out.println("    2  INACTIVE");
        System.out.println("    3  EXPIRED");
        System.out.println("    4  REVOKED");
        System.out.println();
        System.out.print("  > ");
        String choice = scanner.nextLine().trim();

        UserStatus status = switch (choice) {
            case "1" -> UserStatus.ACTIVE;
            case "2" -> UserStatus.INACTIVE;
            case "3" -> UserStatus.EXPIRED;
            case "4" -> UserStatus.REVOKED;
            default -> null;
        };

        if (status == null) {
            System.out.println("  [!] Invalid selection.");
            return;
        }

        System.out.println("  Transitioning...");

        try {
            DigitalIdentityEntity entity = governmentPortal.changeStatus(id, status);
            System.out.println("  Done. Status is now: " + entity.getStatus());
        } catch (IllegalStateException e) {
            System.out.println("  [BLOCKED] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  [NOT FOUND] " + e.getMessage());
        }
    }

    private void lookupStatus() {
        System.out.println();
        System.out.println("  Query the current status of an identity.");
        UUID id = promptForId();
        if (id == null) return;

        try {
            UserStatus status = governmentPortal.lookupStatus(id);
            System.out.println("  Current status: " + status);
        } catch (IllegalArgumentException e) {
            System.out.println("  [NOT FOUND] " + e.getMessage());
        }
    }


    private void handleTaxPortal() {
        System.out.println();
        System.out.println("  === HMRC Portal ===");
        System.out.println("  Verify identity was active during a tax period.");
        System.out.println();

        UUID id = promptForId();
        if (id == null) return;

        System.out.print("  period start (yyyy-mm-dd): ");
        LocalDate start = parseDate(scanner.nextLine().trim());
        if (start == null) return;
        System.out.print("  period end (yyyy-mm-dd): ");
        LocalDate end = parseDate(scanner.nextLine().trim());
        if (end == null) return;

        System.out.println();
        System.out.println("  Checking records...");

        try {
            VerificationResponse response = taxPortal.verifyForPeriod(id, start, end);
            printVerificationResult(response);
        } catch (IllegalArgumentException e) {
            System.out.println("  [NOT FOUND] " + e.getMessage());
        }
    }


    private void handleDrivingLicencePortal() {
        System.out.println();
        System.out.println("  === DVLA Portal ===");
        System.out.println("  Verify identity and driving eligibility.");
        System.out.println();

        UUID id = promptForId();
        if (id == null) return;

        System.out.println();
        System.out.println("  Running eligibility check...");

        try {
            VerificationResponse response = drivingLicencePortal.verifyEligibility(id);
            printVerificationResult(response);
        } catch (IllegalArgumentException e) {
            System.out.println("  [NOT FOUND] " + e.getMessage());
        }
    }


    private void handleEmployerPortal() {
        System.out.println();
        System.out.println("  === Employer Portal ===");
        System.out.println("  Confirm identity validity for employment.");
        System.out.println();

        UUID id = promptForId();
        if (id == null) return;

        System.out.println();
        System.out.println("  Validating...");

        try {
            VerificationResponse response = employerPortal.verifyIdentity(id);
            printVerificationResult(response);
        } catch (IllegalArgumentException e) {
            System.out.println("  [NOT FOUND] " + e.getMessage());
        }
    }


    private UUID promptForId() {
        System.out.println("  Enter the citizen's digital ID (UUID from registration).");
        System.out.print("  digital-id: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("  [!] No ID entered.");
            return null;
        }
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            System.out.println("  [!] Invalid format. Expected: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
            return null;
        }
    }

    private LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            System.out.println("  [!] Could not parse date. Expected format: yyyy-mm-dd");
            return null;
        }
    }

    private void printIdentity(DigitalIdentityEntity entity) {
        System.out.println("  ┌─────────────────────────────────────────");
        System.out.println("  │ id:          " + entity.getDigitalId());
        System.out.println("  │ name:        " + entity.getFirstName() + " " + entity.getLastName());
        System.out.println("  │ email:       " + entity.getEmail());
        System.out.println("  │ dob:         " + entity.getDateOfBirth());
        System.out.println("  │ nationality: " + entity.getNationality());
        System.out.println("  │ status:      " + entity.getStatus());
        System.out.println("  │ expires:     " + entity.getExpiryDate());
        System.out.println("  └─────────────────────────────────────────");
    }

    private void printVerificationResult(VerificationResponse response) {
        System.out.println();
        if (response.verified()) {
            System.out.println("  [PASS] Verification successful.");
        } else {
            System.out.println("  [FAIL] Verification unsuccessful.");
        }
        System.out.println("    reason: " + response.message());
        System.out.println("    at:     " + response.verifiedAt());
    }
}
