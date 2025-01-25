package org.bp.ui.model.order;


import java.time.OffsetDateTime;

public class Point {
    private String address;
    private OffsetDateTime date;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Point address(String address) {
        this.address = address;
        return this;
    }

    public Point date(OffsetDateTime date) {
        this.date = date;
        return this;
    }
}
