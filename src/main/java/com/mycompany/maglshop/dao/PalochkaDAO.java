/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

import com.mycompany.maglshop.model.Palochka;
import com.mycompany.maglshop.DBConnection;
import java.sql.*;

public class PalochkaDAO {
    public void addPalochka(Palochka palochka) throws SQLException {
        String sql = "INSERT INTO palochka(wood_id, core_id) VALUES(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, palochka.getWoodId());
            pstmt.setInt(2, palochka.getCoreId());
            pstmt.executeUpdate();
        }
    }
}