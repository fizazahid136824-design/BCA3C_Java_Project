package com.restaurant.model;

public class RestaurantTable {
    private int id;
    private int tableNumber;
    private String status;

    public RestaurantTable(int id, int tableNumber, String status) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getStatus() {
        return status;
    }
}
