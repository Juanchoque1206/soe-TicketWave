package com.soe.jcb.eventdriven.demo.venue.dto;

import com.soe.jcb.eventdriven.demo.venue.entity.Seat;
import com.soe.jcb.eventdriven.demo.venue.entity.SeatStatus;

public record SeatResponse(
        Long id,
        String row,
        int number,
        SeatStatus status
) {
    public static SeatResponse from(Seat seat, SeatStatus status) {
        return new SeatResponse(
                seat.getId(),
                seat.getRow(),
                seat.getNumber(),
                status
        );
    }
}
