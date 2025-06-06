/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

/**
 *
 * @author nyaku
 */


import com.mycompany.maglshop.model.Wood;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WoodDAO {
    public void addComponent(Wood wood) throws SQLException, IOException {
        String sql = "INSERT INTO wood(name, stock) VALUES(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, wood.getName());
            pstmt.setInt(2, wood.getStock());
            pstmt.executeUpdate();
        }
    }

    public void updateStock(Wood wood) throws SQLException, IOException {
        String sql = "UPDATE wood SET stock = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, wood.getStock());
            pstmt.setInt(2, wood.getId());
            pstmt.executeUpdate();
        }
    }

    public List<Wood> getAll() throws SQLException, IOException {
        List<Wood> woods = new ArrayList<>();
        String sql = "SELECT * FROM wood";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                woods.add(new Wood(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock")
                ));
            }
        }
        return woods;
    }

    public Wood getById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM wood WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Wood(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock")
                );
            }
        }
        return null;
    }

    public Wood getByName(String name) throws SQLException, IOException {
        String sql = "SELECT * FROM wood WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Wood(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock")
                );
            }
        }
        return null;
    }

    public int getId(String name) throws SQLException, IOException {
        String sql = "SELECT id FROM wood WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }

    public void deleteAll() throws SQLException, IOException {
        String sql = "DELETE FROM wood";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}