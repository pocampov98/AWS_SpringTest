package com.example.SpringTest3.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Hamburger {

    private final UUID id;
    private final String menuName;
    private String description;
    private double price;

    public Hamburger(@JsonProperty("id")UUID id, @JsonProperty("menuName")String menuName,
                     @JsonProperty("description") String description, @JsonProperty("price")double price) {
        this.id = id;
        this.menuName = menuName;
        this.description = description;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
