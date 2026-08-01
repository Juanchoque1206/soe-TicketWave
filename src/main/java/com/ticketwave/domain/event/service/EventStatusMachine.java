package com.ticketwave.domain.event.service;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.event.model.EventStatus;

public class EventStatusMachine {

    public void validateTransition(EventStatus current, EventStatus target) {
        boolean valid = switch (current) {
            case DRAFT -> target == EventStatus.PUBLISHED || target == EventStatus.CANCELLED;
            case PUBLISHED -> target == EventStatus.POSTPONED || target == EventStatus.CANCELLED || target == EventStatus.COMPLETED;
            case POSTPONED -> target == EventStatus.PUBLISHED || target == EventStatus.CANCELLED;
            case CANCELLED, COMPLETED -> false;
        };

        if (!valid) {
            throw new BusinessRuleException(
                    String.format("Invalid status transition from %s to %s", current, target));
        }
    }
}
