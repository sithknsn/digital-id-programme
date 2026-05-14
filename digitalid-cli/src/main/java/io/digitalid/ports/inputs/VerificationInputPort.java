package io.digitalid.ports.inputs;
import java.util.UUID;
import io.digitalid.domain.UserStatus;
import io.digitalid.domain.VerificationResponse;
import io.digitalid.strategies.VerificationStrategy;



public interface VerificationInputPort {
    UserStatus checkStatus(UUID digitalid);
    VerificationResponse verify(UUID id, VerificationStrategy strategy);

}
