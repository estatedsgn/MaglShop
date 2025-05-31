/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.maglshop;

/**
 *
 * @author nyaku
 */




import java.sql.*;

public class MaglShop {
    public static void main(String[] args) {
        try {
            createTables(); 
            new GUI().setVisible(true); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables() throws Exception {
        String sqlComponent = """
            CREATE TABLE IF NOT EXISTS component (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                type TEXT NOT NULL CHECK(type IN ('wood', 'core')),
                stock_quantity INTEGER DEFAULT 0
            );
        """;

        String sqlPalochka = """
            CREATE TABLE IF NOT EXISTS palochka (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                wood_id INTEGER NOT NULL,
                core_id INTEGER NOT NULL,
                FOREIGN KEY(wood_id) REFERENCES component(id),
                FOREIGN KEY(core_id) REFERENCES component(id)
            );
        """;

        String sqlBuyer = """
            CREATE TABLE IF NOT EXISTS buyer (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                address TEXT
            );
        """;

        String sqlSupply = """
            CREATE TABLE IF NOT EXISTS supply (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                component_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                date TEXT NOT NULL,
                FOREIGN KEY(component_id) REFERENCES component(id)
            );
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlComponent);
            stmt.execute(sqlPalochka);
            stmt.execute(sqlBuyer);
            stmt.execute(sqlSupply);
        }
    }
}