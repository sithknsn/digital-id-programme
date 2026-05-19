package io.digitalid;

import io.digitalid.adapters.inbound.console.IdentityConsole;
import io.digitalid.adapters.inbound.portals.DrivingLicencePortal;
import io.digitalid.adapters.inbound.portals.EmployerPortal;
import io.digitalid.adapters.inbound.portals.GovernmentPortal;
import io.digitalid.adapters.inbound.portals.TaxPortal;
import io.digitalid.adapters.outbound.FileEventLog;
import io.digitalid.adapters.outbound.FileIdentityStore;
import io.digitalid.domain.AttributeValidator;
import io.digitalid.ports.inputs.IdentityInputPort;
import io.digitalid.ports.inputs.VerificationInputPort;
import io.digitalid.ports.outputs.EventLogPort;
import io.digitalid.ports.outputs.IdentityStorePort;
import io.digitalid.services.IdentityService;
import io.digitalid.services.VerificationService;


public class Main {
    public static void main(String[] args) {
        IdentityStorePort store = new FileIdentityStore("identities.dat");
        EventLogPort eventLog = new FileEventLog("events.log");

        AttributeValidator validator = new AttributeValidator();
        IdentityInputPort identityService = new IdentityService(store, eventLog, validator);
        VerificationInputPort verificationService = new VerificationService(store, eventLog);

        GovernmentPortal govPortal = new GovernmentPortal(identityService, verificationService);
        TaxPortal taxPortal = new TaxPortal(verificationService, eventLog);
        DrivingLicencePortal dvlaPortal = new DrivingLicencePortal(verificationService);
        EmployerPortal employerPortal = new EmployerPortal(verificationService);

        new IdentityConsole(govPortal, taxPortal, dvlaPortal, employerPortal).run();
    }
}
