package io.digitalid.strategies;

import io.digitalid.domain.DigitalIdentityEntity;
import io.digitalid.domain.EventEntry;
import io.digitalid.domain.UserStatus;
import io.digitalid.domain.VerificationResponse;
import io.digitalid.ports.outputs.EventLogPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TaxAuthorityStrategy implements VerificationStrategy {

    private final LocalDate periodStart;
    private final LocalDate periodEnd;
    private final EventLogPort eventLog;

    public TaxAuthorityStrategy(LocalDate periodStart, LocalDate periodEnd, EventLogPort eventLog) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.eventLog = eventLog;
    }

    @Override
    public VerificationResponse verify(DigitalIdentityEntity identity) {
        if (identity.getStatus() != UserStatus.ACTIVE) {
            return new VerificationResponse(
                identity.getDigitalId(),
                false,
                LocalDateTime.now(),
                "Identity is not currently active"
            );
        }

        List<EventEntry> events = eventLog.retrieveEvents(identity.getDigitalId());
        boolean isInactive = events.stream()
            .filter(e -> e.action().equals("status_change"))
            .filter(e -> isWithinPeriod(e.timestamp()))
            .anyMatch(e -> e.details().contains("INACTIVE") || e.details().contains("EXPIRED"));

        if (isInactive) {
            return new VerificationResponse(
                identity.getDigitalId(),
                false,
                LocalDateTime.now(),
                "Identity was inactive during reporting period " + periodStart + " to " + periodEnd
            );
        }

        return new VerificationResponse(
            identity.getDigitalId(),
            true,
            LocalDateTime.now(),
            "Identity active throughout reporting period"
        );
    }

    private boolean isWithinPeriod(LocalDateTime timestamp) {
        LocalDate date = timestamp.toLocalDate();
        return !date.isBefore(periodStart) && !date.isAfter(periodEnd);
    }
}
