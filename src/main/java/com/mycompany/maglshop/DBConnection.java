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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_NAME = "wand_shop.db";
    private static final String RESOURCE_DB_PATH = "/" + DB_NAME;
    
    public static Connection getConnection() throws SQLException {
        try {
            // Путь к БД в рабочей директории
            Path dbPath = Paths.get(DB_NAME);
            
            // Если БД не существует в рабочей директории, копируем из ресурсов
            if (!Files.exists(dbPath)) {
                copyDatabaseFromResources(dbPath);
            }
            
            // Подключаемся к БД в рабочей директории
            String url = "jdbc:sqlite:" + dbPath.toAbsolutePath().toString();
            return DriverManager.getConnection(url);
            
        } catch (Exception e) {
            throw new SQLException("Ошибка подключения к базе данных: " + e.getMessage(), e);
        }
    }
    
    private static void copyDatabaseFromResources(Path targetPath) throws IOException {
        try (InputStream inputStream = DBConnection.class.getResourceAsStream(RESOURCE_DB_PATH)) {
            if (inputStream == null) {
                // Если БД нет в ресурсах, создаем пустую
                System.out.println("База данных не найдена в ресурсах. Будет создана новая.");
                return;
            }
            
            // Копируем БД из ресурсов в рабочую директорию
            Files.copy(inputStream, targetPath);
            System.out.println("База данных скопирована из ресурсов: " + targetPath.toAbsolutePath());
        }
    }
}