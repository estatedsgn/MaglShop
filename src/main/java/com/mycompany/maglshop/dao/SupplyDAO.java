/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

import com.mycompany.maglshop.model.Supply;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;
import java.sql.*;

public class SupplyDAO {
    public void addSupply(Supply supply) throws SQLException, IOException {
        String sql = "INSERT INTO supply(component_id, quantity, date) VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supply.getComponentId());
            pstmt.setInt(2, supply.getQuantity());
            pstmt.setString(3, supply.getDate());
            pstmt.executeUpdate();
        }
    }
}