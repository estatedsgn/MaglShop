/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

/**
 *
 * @author nyaku
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import com.mycompany.maglshop.model.MagicStick;
import com.mycompany.maglshop.model.Wood;
import com.mycompany.maglshop.model.Core;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;
import java.sql.*;

public class MagicStickDAO {
    public void addStick(MagicStick stick) throws SQLException, IOException {
        String sql = "INSERT INTO magic_stick(wood_id, core_id, stock) VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stick.getWood().getId());
            pstmt.setInt(2, stick.getCore().getId());
            pstmt.setInt(3, stick.getStock());
            pstmt.executeUpdate();
        }
    }

    public List<MagicStick> getAll() throws SQLException, IOException {
        List<MagicStick> sticks = new ArrayList<>();
        String sql = "SELECT * FROM magic_stick";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Wood wood = new WoodDAO().getById(rs.getInt("wood_id"));
                Core core = new CoreDAO().getById(rs.getInt("core_id"));
                sticks.add(new MagicStick(
                    rs.getInt("id"),
                    wood,
                    core,
                    rs.getInt("stock")
                ));
            }
        }
        return sticks;
    }

    public MagicStick getById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM magic_stick WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Wood wood = new WoodDAO().getById(rs.getInt("wood_id"));
                Core core = new CoreDAO().getById(rs.getInt("core_id"));
                return new MagicStick(
                    rs.getInt("id"),
                    wood,
                    core,
                    rs.getInt("stock")
                );
            }
        }
        return null;
    }

    public void deleteAll() throws SQLException, IOException {
        String sql = "DELETE FROM magic_stick";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}