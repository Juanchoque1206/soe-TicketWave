package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.infrastructure.persistence.JpaBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seats")
public class JpaSeat extends JpaBaseEntity {

    @Column(name = "seat_row", nullable = false)
    private String row;

    @Column(name = "seat_number", nullable = false)
    private int number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private JpaSection section;

    public JpaSeat() {}

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public JpaSection getSection() {
        return section;
    }

    public void setSection(JpaSection section) {
        this.section = section;
    }
}
