package com.restaurant.dao;

import com.restaurant.model.RestaurantTable;
import com.restaurant.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {

    public void addTable(RestaurantTable table) {
        String sql = "INSERT INTO restaurant_tables (table_number, status) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, table.getTableNumber());
            ps.setString(2, table.getStatus());

            ps.executeUpdate();
            System.out.println("Table added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RestaurantTable> getAllTables() {
        List<RestaurantTable> tables = new ArrayList<>();

        String sql = "SELECT * FROM restaurant_tables";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                tables.add(new RestaurantTable(
                        rs.getInt("id"),
                        rs.getInt("table_number"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tables;
    }
}
