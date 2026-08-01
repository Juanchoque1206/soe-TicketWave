package com.ticketwave.domain.venue.model;

import com.ticketwave.domain.common.model.BaseEntity;

public class Seat extends BaseEntity {

    private String row;
    private int number;
    private Long sectionId;

    public Seat() {}

    public String getRow() { return row; }
    public void setRow(String row) { this.row = row; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
}
