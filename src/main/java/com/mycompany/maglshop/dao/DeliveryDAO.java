/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

/**
 *
 * @author nyaku
 */

import com.mycompany.maglshop.model.Delivery;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDAO {
    public void addDelivery(Delivery delivery) throws SQLException, IOException {
        String sql = "INSERT INTO delivery(component_type, component_id, quantity, date) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, delivery.getComponentType());
            pstmt.setInt(2, delivery.getComponentId());
            pstmt.setInt(3, delivery.getQuantity());
            pstmt.setString(4, delivery.getDate());
            pstmt.executeUpdate();
        }
    }

    public List<Delivery> getAll() throws SQLException, IOException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM delivery";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                deliveries.add(new Delivery(
                    rs.getInt("id"),
                    rs.getInt("component_id"),
                    rs.getInt("quantity"),
                        rs.getString("component_type")
                ));
            }
        }
        return deliveries;
    }

    public void deleteAll() throws SQLException, IOException {
        String sql = "DELETE FROM delivery";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}