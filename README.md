# Digital ID Programme

A console-based backend system for managing and verifying digital identities across companies and organisations. Built using hexagonal (ports and adapters) architecture with the strategy pattern for verification logic.

**Github Repository:** https://github.com/sithknsn/digital-id-programme


<img width="418" height="401" alt="image" src="https://github.com/user-attachments/assets/6d820b5c-b47f-440c-80bb-733230edf919" />



**Running the application:**

```bash
cd digitalid-cli
mvn compile exec:java -q
```

**Run tests:**

```bash
cd digitalid-cli
mvn test
```

## System Structure

Domain logic can only be accessed through port interfaces and adapters on either side handle I/O (hexagonal architecture)

```
domain/         -> Entity, state machine, validation, records
ports/inputs/   -> Interfaces for identity management and verification
ports/outputs/  -> Interfaces for persistence and event logging
services/       -> Implements inbound ports, domain operations
strategies/     -> verification logic per organisation type
adapters/       -> Portals (inbound), file storage and logging (outbound), CLI console
Main.java       -> Composition for wiring portals
```


- **DigitalIdentityEntity**: holds identity data, state transitions, tracks expiry.
- **UserStatus**: enum-based state handling. PENDING, ACTIVE, INACTIVE, EXPIRED, REVOKED. 
- **IdentityService / VerificationService**: implement the two inbound ports. (Validate, operate, persist, log)
- **Verification strategies**:  TaxAuthorityStrategy (period history check), DrivingLicenceStrategy (age check), EmployerStrategy (active/inactive only).
- **Portals**: GovernmentPortal has full access. Tax, DVLA, Employer portals can only verify. Authorisation enforced by which ports each portal receives.
- **FileIdentityStore / FileEventLog**: persist identities and events to disk.
- **IdentityConsole**: CLI interface. Reads input and enforces guards by direcitng to portals.
