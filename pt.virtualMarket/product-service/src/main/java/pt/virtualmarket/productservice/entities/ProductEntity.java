package pt.virtualmarket.productservice.entities;

import java.math.BigDecimal;

public class ProductEntity {
    private String name;
    private String description;
    private BigDecimal price;
    private  boolean availability;


    public ProductEntity() {
    }

    public ProductEntity(String name, String description, BigDecimal price, boolean availability) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailability() {
        return availability;
    }
}
