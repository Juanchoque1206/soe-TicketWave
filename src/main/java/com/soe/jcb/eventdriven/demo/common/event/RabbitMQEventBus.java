package com.soe.jcb.eventdriven.demo.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "eventbus.type", havingValue = "rabbitmq")
public class RabbitMQEventBus implements DomainEventBus {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQEventBus.class);

    public static final String EXCHANGE = "ticketwave.events";

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventBus(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        String routingKey = event.getRoutingKey();
        log.info("Publishing event to RabbitMQ: {} (routingKey={})",
                event.getClass().getSimpleName(), routingKey);
        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, event);
    }
}
