/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop;

import com.mycompany.maglshop.DBConnection;
import javax.swing.*;
import java.awt.*;

import com.mycompany.maglshop.dao.*;
import com.mycompany.maglshop.model.*;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {
    private JTable supplyTable;
    private JTable stockTable;
    private DefaultTableModel supplyTableModel;
    private DefaultTableModel stockTableModel;

    public GUI() {
        setTitle("MaglShop - Учетная система");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель действий
        JPanel actionPanel = new JPanel(new GridLayout(0, 1));
        JButton addComponentBtn = new JButton("Добавить компонент");
        JButton addPalochkaBtn = new JButton("Добавить палочку");
        JButton addBuyerBtn = new JButton("Добавить покупателя");
        JButton addSupplyBtn = new JButton("Добавить поставку");
        JButton resetBtn = new JButton("Очистить всё");

        actionPanel.add(addComponentBtn);
        actionPanel.add(addPalochkaBtn);
        actionPanel.add(addBuyerBtn);
        actionPanel.add(addSupplyBtn);
        actionPanel.add(resetBtn);

        // Таблица поставок
        supplyTableModel = new DefaultTableModel();
        supplyTableModel.addColumn("ID");
        supplyTableModel.addColumn("Компонент");
        supplyTableModel.addColumn("Количество");
        supplyTableModel.addColumn("Дата");
        updateSupplyTable();

        supplyTable = new JTable(supplyTableModel);
        JScrollPane supplyScrollPane = new JScrollPane(supplyTable);

        // Таблица склада
        stockTableModel = new DefaultTableModel();
        stockTableModel.addColumn("ID");
        stockTableModel.addColumn("Компонент");
        stockTableModel.addColumn("Тип");
        stockTableModel.addColumn("Количество");
        updateStockTable();

        stockTable = new JTable(stockTableModel);
        JScrollPane stockScrollPane = new JScrollPane(stockTable);

        // Панель таблиц
        JSplitPane tableSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, supplyScrollPane, stockScrollPane);
        tableSplitPane.setDividerLocation(600);

        // Главная панель
        JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, actionPanel, tableSplitPane);
        mainPanel.setDividerLocation(300);

        add(mainPanel, BorderLayout.CENTER);

        // Обработчики событий
        addComponentBtn.addActionListener(e -> addComponentDialog());
        addPalochkaBtn.addActionListener(e -> addPalochkaDialog());
        addBuyerBtn.addActionListener(e -> addBuyerDialog());
        addSupplyBtn.addActionListener(e -> {
            try {
                addSupplyDialog();
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        resetBtn.addActionListener(e -> fullReset());
    }

    private void addComponentDialog() {
        String name = JOptionPane.showInputDialog("Название компонента:");
        if (name == null || name.trim().isEmpty()) return;

        Object[] typeOptions = {"wood", "core"};
        String type = (String) JOptionPane.showInputDialog(this, "Выберите тип:", "Тип компонента", 
            JOptionPane.QUESTION_MESSAGE, null, typeOptions, "wood");

        if (type == null) return;
        String qtyStr = JOptionPane.showInputDialog("Количество:");
        int qty = Integer.parseInt(qtyStr);

        try {
            new ComponentDAO().addComponent(new Component1(0, name, type, qty));
            JOptionPane.showMessageDialog(this, "Компонент добавлен!");
            updateStockTable();
            updateSupplyTable();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void addPalochkaDialog() {
        String woodName = JOptionPane.showInputDialog("Древесина (название):");
        String coreName = JOptionPane.showInputDialog("Сердцевина (название):");

        int woodId = getComponentId("wood", woodName);
        int coreId = getComponentId("core", coreName);

        if (woodId == -1 || coreId == -1) {
            JOptionPane.showMessageDialog(this, "Компоненты не найдены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            new PalochkaDAO().addPalochka(new Palochka(0, woodId, coreId));
            new ComponentDAO().updateStock(woodId, getStock(woodId) - 1);
            new ComponentDAO().updateStock(coreId, getStock(coreId) - 1);
            JOptionPane.showMessageDialog(this, "Палочка добавлена!");
            updateStockTable();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void addBuyerDialog() {
        String name = JOptionPane.showInputDialog("Имя покупателя:");
        String address = JOptionPane.showInputDialog("Адрес:");

        try {
            new BuyerDAO().addBuyer(new Buyer(0, name, address));
            JOptionPane.showMessageDialog(this, "Покупатель добавлен!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void addSupplyDialog() throws SQLException {
        String componentName = JOptionPane.showInputDialog("Название компонента:");
        if (componentName == null) return;

        Object[] typeOptions = {"wood", "core"};
        String type = (String) JOptionPane.showInputDialog(this, "Выберите тип:", "Тип компонента", 
            JOptionPane.QUESTION_MESSAGE, null, typeOptions, "wood");

        if (type == null) return;

        int componentId = getComponentId(type, componentName);
        if (componentId == -1) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Компонент не найден. Создать новый?", "Создать компонент", 
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                new ComponentDAO().addComponent(new Component1(0, componentName, type, 0));
                componentId = getComponentId(type, componentName);
            } else {
                return;
            }
        }

        String qtyStr = JOptionPane.showInputDialog("Количество:");
        int qty = Integer.parseInt(qtyStr);
        String date = JOptionPane.showInputDialog("Дата поставки (ГГГГ-ММ-ДД):");

        try {
            new SupplyDAO().addSupply(new Supply(0, componentId, qty, date));
            new ComponentDAO().updateStock(componentId, getStock(componentId) + qty);
            JOptionPane.showMessageDialog(this, "Поставка добавлена!");
            updateStockTable();
            updateSupplyTable();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateSupplyTable() {
        supplyTableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT s.id, c.name, s.quantity, s.date " +
                "FROM supply s JOIN component c ON s.component_id = c.id")) {
            
            while (rs.next()) {
                supplyTableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getString("date")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStockTable() {
        stockTableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, type, stock_quantity FROM component")) {
            
            while (rs.next()) {
                stockTableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getInt("stock_quantity")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getComponentId(String type, String name) {
        String sql = "SELECT id FROM component WHERE name = ? AND type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getStock(int componentId) {
        String sql = "SELECT stock_quantity FROM component WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, componentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("stock_quantity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void fullReset() {
        try {
            String[] tables = {"supply", "buyer", "palochka", "component"};
            for (String table : tables) {
                try (Connection conn = DBConnection.getConnection();
                     Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("DELETE FROM " + table);
                    stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name = '" + table + "'");
                }
            }
            JOptionPane.showMessageDialog(this, "Данные очищены!");
            updateStockTable();
            updateSupplyTable();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, "Ошибка: " + message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}