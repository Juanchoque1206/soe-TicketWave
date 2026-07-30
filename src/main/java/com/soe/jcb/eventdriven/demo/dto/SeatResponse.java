package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.Seat;
import com.soe.jcb.eventdriven.demo.entity.SeatStatus;

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
