/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.maglshop.model.MagicStick;
import com.mycompany.maglshop.model.Wood;
import com.mycompany.maglshop.model.Core;
import com.mycompany.maglshop.DBConnection;
import java.io.IOException;

public class MagicStickDAO {
    

    public void markAsSold(int id) throws SQLException, IOException {
        String sql = "UPDATE magic_stick SET sold = true WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Изменить метод getAll:
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
                    rs.getInt("stock"),
                    rs.getBoolean("sold") // Добавляем поле sold
                ));
            }
        }
        return sticks;
    }

    // Изменить метод getById:
    public MagicStick getById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM magic_stick WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Wood wood = new WoodDAO().getById(rs.getInt("wood_id"));
                    Core core = new CoreDAO().getById(rs.getInt("core_id"));
                    return new MagicStick(
                        rs.getInt("id"),
                        wood,
                        core,
                        rs.getInt("stock"),
                        rs.getBoolean("sold") // Добавляем поле sold
                    );
                }
            }
        }
        return null;
    }

    // Изменить метод addStick:
    public void addStick(MagicStick stick) throws SQLException, IOException {
        // Проверяем, не существует ли уже палочка с такой комбинацией wood и core
        if (stickExists(stick.getWood().getId(), stick.getCore().getId())) {
            throw new IllegalArgumentException("Палочка с такой комбинацией дерева и сердцевины уже существует!");
        }

        String sql = "INSERT INTO magic_stick(wood_id, core_id, stock, sold) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, stick.getWood().getId());
            pstmt.setInt(2, stick.getCore().getId());
            pstmt.setInt(3, 1); // Всегда устанавливаем stock = 1
            pstmt.setBoolean(4, false); // По умолчанию не продана
            pstmt.executeUpdate();

            // Получаем сгенерированный id и устанавливаем его в объект
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    stick.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    // Метод для проверки существования палочки с данной комбинацией
    private boolean stickExists(int woodId, int coreId) throws SQLException, IOException {
        String sql = "SELECT COUNT(*) FROM magic_stick WHERE wood_id = ? AND core_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, woodId);
            pstmt.setInt(2, coreId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }




    
    // Метод для получения палочки по комбинации дерева и сердцевины
    public MagicStick getByWoodAndCore(int woodId, int coreId) throws SQLException, IOException {
        String sql = "SELECT * FROM magic_stick WHERE wood_id = ? AND core_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, woodId);
            pstmt.setInt(2, coreId);
            try (ResultSet rs = pstmt.executeQuery()) {
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
    public void deleteById(int id) throws SQLException, IOException {
        String sql = "DELETE FROM magic_stick WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}