package com.soe.jcb.eventdriven.demo.ticket.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.ticket.application.out.TicketOrderQueryPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adapter resolving order-item metadata for ticket responses by querying the
 * shared read model across order/event/venue tables. Kept at the infrastructure
 * layer so the ticket domain stays decoupled.
 */
@Component
public class TicketOrderQueryJdbcAdapter implements TicketOrderQueryPort {

    private final JdbcTemplate jdbcTemplate;

    public TicketOrderQueryJdbcAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<OrderItemSnapshot> findOrderItemsByOrderId(Long orderId) {
        String sql = """
                SELECT oi.id                  AS order_item_id,
                       e.name                 AS event_name,
                       e.event_date           AS event_date,
                       v.name                 AS venue_name,
                       tt.name                AS ticket_type_name,
                       oi.seat_row            AS seat_row,
                       oi.seat_number         AS seat_number
                FROM order_items oi
                JOIN orders o ON o.id = oi.order_id
                JOIN ticket_types tt ON tt.id = oi.ticket_type_id
                JOIN events e ON e.id = tt.event_id
                LEFT JOIN venues v ON v.id = e.venue_id
                WHERE oi.order_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new OrderItemSnapshot(
                rs.getLong("order_item_id"),
                rs.getString("event_name"),
                rs.getTimestamp("event_date") != null
                        ? rs.getTimestamp("event_date").toLocalDateTime() : null,
                rs.getString("venue_name"),
                rs.getString("ticket_type_name"),
                rs.getString("seat_row"),
                rs.getObject("seat_number") != null ? rs.getInt("seat_number") : null
        ), orderId);
    }
}