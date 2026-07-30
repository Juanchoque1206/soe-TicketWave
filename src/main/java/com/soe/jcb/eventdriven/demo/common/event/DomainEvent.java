package com.soe.jcb.eventdriven.demo.common.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public interface DomainEvent extends Serializable {

    UUID eventId();

    LocalDateTime occurredOn();

    String getRoutingKey();
}
