/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

/**
 *
 * @author nyaku
 */

import com.mycompany.maglshop.model.Sale;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    public void addSale(Sale sale) throws SQLException, IOException {
        String sql = "INSERT INTO sale(stick_id, buyer_id, date) VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sale.getStickId());
            pstmt.setInt(2, sale.getBuyerId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            pstmt.setString(3, sale.getDate()); 
            pstmt.executeUpdate();
        }
    }

    public List<Sale> getAll() throws SQLException, IOException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sale";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("id"),
                    rs.getInt("stick_id"),
                    rs.getInt("buyer_id"),
                    rs.getString("date")
                ));
            }
        }
        return sales;
    }

    public void deleteAll() throws SQLException, IOException {
        String sql = "DELETE FROM sale";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
