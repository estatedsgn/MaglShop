/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

/**
 *
 * @author nyaku
 */

import com.mycompany.maglshop.model.Purchase;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;
import java.sql.*;

public class PurchaseDAO {
    public void addPurchase(Purchase purchase) throws SQLException, IOException {
        String sql = "INSERT INTO purchase(buyer_id, palochka_id, date) VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, purchase.getBuyerId());
            pstmt.setInt(2, purchase.getPalochkaId());
            pstmt.setString(3, purchase.getDate());
            pstmt.executeUpdate();
        }
    }
}
