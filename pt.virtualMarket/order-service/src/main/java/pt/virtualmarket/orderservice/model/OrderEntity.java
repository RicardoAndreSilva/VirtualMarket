package pt.virtualmarket.orderservice.model;

import java.math.BigDecimal;

public class OrderEntity {
    private int quantity;
    private BigDecimal totalPrice;

    public OrderEntity() {
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
