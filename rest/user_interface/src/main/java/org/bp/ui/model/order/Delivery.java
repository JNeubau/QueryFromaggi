package org.bp.ui.model.order;

import java.time.OffsetDateTime;

public class Delivery {
    private Point from;
    private Point to;

    public Point getFrom() {
        return from;
    }

    public void setFrom(Point from) {
        this.from = from;
    }

    public Point getTo() {
        return to;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    public Delivery() {
        this.from = new Point().address("Poznan").date(OffsetDateTime.now());
    }
}
