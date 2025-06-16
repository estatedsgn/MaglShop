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
            createTables(); // Создание таблиц при первом запуске
            new GUI().setVisible(true); // Запуск интерфейса
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables() throws Exception {
        String sqlWood = """
            CREATE TABLE IF NOT EXISTS wood (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                stock INTEGER DEFAULT 0
            );
        """;

        String sqlCore = """
            CREATE TABLE IF NOT EXISTS core (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                stock INTEGER DEFAULT 0
            );
        """;

        String sqlStick = """
            CREATE TABLE IF NOT EXISTS magic_stick (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                wood_id INTEGER NOT NULL,
                core_id INTEGER NOT NULL,
                stock INTEGER DEFAULT 0,
                sold BOOLEAN DEFAULT FALSE,
                FOREIGN KEY(wood_id) REFERENCES wood(id),
                FOREIGN KEY(core_id) REFERENCES core(id)
            );
        """;

        String sqlBuyer = """
            CREATE TABLE IF NOT EXISTS buyer (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                address TEXT
            );
        """;

        String sqlSale = """
            CREATE TABLE IF NOT EXISTS sale (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                stick_id INTEGER NOT NULL,
                buyer_id INTEGER NOT NULL,
                date TEXT NOT NULL,
                FOREIGN KEY(stick_id) REFERENCES magic_stick(id),
                FOREIGN KEY(buyer_id) REFERENCES buyer(id)
            );
        """;

        String sqlDelivery = """
            CREATE TABLE IF NOT EXISTS delivery (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                component_type TEXT NOT NULL CHECK(component_type IN ('wood', 'core')),
                component_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                date TEXT NOT NULL,
                FOREIGN KEY(component_id) REFERENCES wood(id),
                FOREIGN KEY(component_id) REFERENCES core(id)
            );
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlWood);
            stmt.execute(sqlCore);
            stmt.execute(sqlStick);
            stmt.execute(sqlBuyer);
            stmt.execute(sqlSale);
            stmt.execute(sqlDelivery);
        }
    }
}