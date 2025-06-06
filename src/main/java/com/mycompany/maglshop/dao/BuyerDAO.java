/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

import com.mycompany.maglshop.model.Buyer;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuyerDAO {
    public void addBuyer(Buyer buyer) throws SQLException, IOException {
        String sql = "INSERT INTO buyer(name, address) VALUES(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, buyer.getName());
            pstmt.setString(2, buyer.getAddress());
            pstmt.executeUpdate();
        }
    }

    public List<Buyer> getAll() throws SQLException, IOException {
        List<Buyer> buyers = new ArrayList<>();
        String sql = "SELECT * FROM buyer";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                buyers.add(new Buyer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address")
                ));
            }
        }
        return buyers;
    }

    public Buyer getById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM buyer WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Buyer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address")
                );
            }
        }
        return null;
    }

    public Buyer getByName(String name) throws SQLException, IOException {
        String sql = "SELECT * FROM buyer WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Buyer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address")
                );
            }
        }
        return null;
    }

    public void deleteAll() throws SQLException, IOException {
        String sql = "DELETE FROM buyer";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}