package org.bp.ui.model.order;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Pizza {

    private String name;
    private Float size;
    private String ingredients;
    private BigDecimal prize;
    private OffsetDateTime prepTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getPrize() {
        return prize;
    }

    public void setPrize(BigDecimal prize) {
        this.prize = prize;
    }

    public OffsetDateTime getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(OffsetDateTime prepTime) {
        this.prepTime = prepTime;
    }
}
