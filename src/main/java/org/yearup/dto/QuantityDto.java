package org.yearup.dto;

public class QuantityDto {
    private int quantity;

    public QuantityDto(int quantity) {
        this.quantity = quantity;
    }

    public QuantityDto() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
