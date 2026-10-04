package com.restaurant.dao;

import com.restaurant.model.Food;
import com.restaurant.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO {

    public void addFood(Food food) {
        String sql = "INSERT INTO food (name, price, category) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, food.getName());
            ps.setDouble(2, food.getPrice());
            ps.setString(3, food.getCategory());

            ps.executeUpdate();
            System.out.println("Food added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Food> getAllFood() {
        List<Food> foods = new ArrayList<>();

        String sql = "SELECT * FROM food";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                foods.add(new Food(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foods;
    }
}
