/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

/**
 *
 * @author nyaku*/


import com.mycompany.maglshop.model.Core;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoreDAO {
    public void addComponent(Core core) throws SQLException, IOException {
            String sql = """
        INSERT INTO core(name, stock) 
        VALUES(?, ?)
        ON CONFLICT(name) 
        DO UPDATE SET stock = excluded.stock
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, core.getName());
            pstmt.setInt(2, core.getStock());
            pstmt.executeUpdate();
        }
    }

    public void updateStock(Core core) throws SQLException, IOException {
        String sql = "UPDATE core SET stock = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, core.getStock());
            pstmt.setInt(2, core.getId());
            pstmt.executeUpdate();
        }
    }

    public List<Core> getAll() throws SQLException, IOException {
        List<Core> cores = new ArrayList<>();
        String sql = "SELECT * FROM core";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cores.add(new Core(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock")
                ));
            }
        }
        return cores;
    }

    public Core getById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM core WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Core(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock")
                );
            }
        }
        return null;
    }

    public Core getByName(String name) throws SQLException, IOException {
        String sql = "SELECT * FROM core WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Core(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock")
                );
            }
        }
        return null;
    }

    public int getId(String name) throws SQLException, IOException {
        String sql = "SELECT id FROM core WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }

    public void deleteAll() throws SQLException, IOException {
        String sql = "DELETE FROM core";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}