package com.soe.jcb.eventdriven.demo.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "eventbus.type", havingValue = "rabbitmq")
public class RabbitMQConfig {

    public static final String EXCHANGE = "ticketwave.events";

    public static final String QUEUE_ORDER = "ticketwave.order";
    public static final String ROUTING_ORDER_SUBMITTED = "order.submitted";
    public static final String ROUTING_ORDER_CONFIRMED = "order.confirmed";
    public static final String ROUTING_ORDER_CANCELLED = "order.cancelled";

    public static final String QUEUE_PAYMENT = "ticketwave.payment";
    public static final String ROUTING_PAYMENT_COMPLETED = "payment.completed";
    public static final String ROUTING_PAYMENT_REFUNDED = "payment.refunded";

    public static final String QUEUE_TICKET = "ticketwave.ticket";
    public static final String ROUTING_TICKET_ISSUED = "ticket.issued";
    public static final String ROUTING_TICKET_VALIDATED = "ticket.validated";

    public static final String QUEUE_NOTIFICATION = "ticketwave.notification";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public DirectExchange ticketwaveExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue orderQueue() { return new Queue(QUEUE_ORDER, true); }

    @Bean
    public Queue paymentQueue() { return new Queue(QUEUE_PAYMENT, true); }

    @Bean
    public Queue ticketQueue() { return new Queue(QUEUE_TICKET, true); }

    @Bean
    public Queue notificationQueue() { return new Queue(QUEUE_NOTIFICATION, true); }

    @Bean
    public Binding orderSubmittedBinding(@Qualifier("orderQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(ROUTING_ORDER_SUBMITTED);
    }

    @Bean
    public Binding orderConfirmedBinding(@Qualifier("orderQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(ROUTING_ORDER_CONFIRMED);
    }

    @Bean
    public Binding orderCancelledBinding(@Qualifier("orderQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(ROUTING_ORDER_CANCELLED);
    }

    @Bean
    public Binding paymentCompletedBinding(@Qualifier("paymentQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(ROUTING_PAYMENT_COMPLETED);
    }

    @Bean
    public Binding paymentRefundedBinding(@Qualifier("paymentQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(ROUTING_PAYMENT_REFUNDED);
    }

    @Bean
    public Binding ticketIssuedBinding(@Qualifier("ticketQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(ROUTING_TICKET_ISSUED);
    }

    @Bean
    public Binding ticketValidatedBinding(@Qualifier("ticketQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(ROUTING_TICKET_VALIDATED);
    }

    @Bean
    public Binding notificationOrderConfirmedBinding(@Qualifier("notificationQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(ROUTING_ORDER_CONFIRMED);
    }

    @Bean
    public Binding notificationOrderCancelledBinding(@Qualifier("notificationQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(ROUTING_ORDER_CANCELLED);
    }
}
