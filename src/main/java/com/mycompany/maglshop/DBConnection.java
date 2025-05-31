/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop;

/**
 *
 * @author nyaku
 */





import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class DBConnection {
    // Абсолютный путь к БД (сохранён как у пользователя)
    private static final String DB_PATH = "src/main/java/resources/database/wand_shop.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            File dbFile = new File(DB_PATH);
            if (!dbFile.exists()) {
                dbFile.getParentFile().mkdirs(); // Создаём папки, если их нет
                dbFile.createNewFile(); // Создаём файл
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации БД: " + e.getMessage(), e);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }
}