package com.soe.jcb.eventdriven.demo.venue.domain;

/**
 * Seat value object of the Venue aggregate. Pure domain - no JPA.
 */
public class Seat {

    private Long id;
    private String row;
    private int number;

    public Seat(Long id, String row, int number) {
        this.id = id;
        this.row = row;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }
}