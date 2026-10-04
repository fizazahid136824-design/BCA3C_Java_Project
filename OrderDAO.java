package com.restaurant.dao;

import com.restaurant.model.Order;
import com.restaurant.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public void addOrder(Order order) {
        String sql = "INSERT INTO orders " +
                "(table_number, food_name, quantity, total_amount) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, order.getTableNumber());
            ps.setString(2, order.getFoodName());
            ps.setInt(3, order.getQuantity());
            ps.setDouble(4, order.getTotalAmount());

            ps.executeUpdate();
            System.out.println("Order added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getInt("table_number"),
                        rs.getString("food_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_amount")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}