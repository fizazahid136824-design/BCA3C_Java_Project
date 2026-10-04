package com.restaurant.model;

public class Order {
    private int id;
    private int tableNumber;
    private String foodName;
    private int quantity;
    private double totalAmount;

    public Order(int id, int tableNumber, String foodName, int quantity, double totalAmount) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.foodName = foodName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}