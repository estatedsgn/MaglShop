/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

import com.mycompany.maglshop.model.Buyer;
import com.mycompany.maglshop.DBConnection;
import java.sql.*;

public class BuyerDAO {
    public void addBuyer(Buyer buyer) throws SQLException {
        String sql = "INSERT INTO buyer(name, address) VALUES(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, buyer.getName());
            pstmt.setString(2, buyer.getAddress());
            pstmt.executeUpdate();
        }
    }
}