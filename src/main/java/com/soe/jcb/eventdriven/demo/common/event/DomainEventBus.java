package com.soe.jcb.eventdriven.demo.common.event;

public interface DomainEventBus {

    void publish(DomainEvent event);
}
