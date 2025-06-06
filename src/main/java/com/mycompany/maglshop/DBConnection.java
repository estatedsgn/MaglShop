/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop;

/**
 *
 * @author nyaku
 */




import java.io.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_NAME = "wand_shop.db";
    private static final String DB_PATH = "database/" + DB_NAME;
    private static final String USER_DIR = System.getProperty("user.home") + File.separator + ".MaglShop";
    private static final String DB_FILE = USER_DIR + File.separator + DB_NAME;

    public static Connection getConnection() throws IOException, SQLException {
        // Создаём пользовательскую папку для хранения БД
        File userDir = new File(USER_DIR);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        // Копируем БД из ресурсов, если её нет в пользовательской папке
        try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream(DB_PATH)) {
            if (is == null) {
                throw new FileNotFoundException("База данных не найдена в ресурсах: " + DB_PATH);
            }

            File dbFile = new File(DB_FILE);
            if (!dbFile.exists()) {
                Files.copy(is, Paths.get(DB_FILE));
            }
        }

        // Подключаемся к БД в пользовательской папке
        return DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);
    }
}