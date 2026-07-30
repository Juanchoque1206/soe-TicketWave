package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.Section;

public record SectionResponse(
        Long id,
        String name,
        int capacity,
        boolean generalAdmission,
        int seatCount
) {
    public static SectionResponse from(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getName(),
                section.getCapacity(),
                section.isGeneralAdmission(),
                section.getSeats().size()
        );
    }
}
