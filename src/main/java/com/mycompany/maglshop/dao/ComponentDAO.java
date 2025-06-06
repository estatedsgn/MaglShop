/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

import com.mycompany.maglshop.model.Component1;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;
import java.sql.*;

public class ComponentDAO {
    public void addComponent(Component1 component) throws SQLException, IOException {
        String sql = "INSERT INTO component(name, type, stock_quantity) VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, component.getName());
            pstmt.setString(2, component.getType());
            pstmt.setInt(3, component.getStockQuantity());
            pstmt.executeUpdate();
        }
    }

    public void updateStock(int componentId, int newQuantity) throws SQLException, IOException {
        String sql = "UPDATE component SET stock_quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, componentId);
            pstmt.executeUpdate();
        }
    }
}