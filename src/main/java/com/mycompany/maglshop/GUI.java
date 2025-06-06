/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mycompany.maglshop.dao.*;
import com.mycompany.maglshop.model.*;
import com.mycompany.maglshop.DBConnection;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {
    private JTable woodTable, coreTable, stickTable, buyerTable, saleTable;
    private DefaultTableModel woodModel, coreModel, stickModel, buyerModel, saleModel;
    private ShopService shopService = new ShopService();

    public GUI() throws Exception {
        setTitle("MaglShop - Учетная система");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Инициализация моделей и таблиц
        initTables();

        // Главное меню
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem resetItem = new JMenuItem("Очистить всё");
        resetItem.addActionListener(e -> fullReset());
        fileMenu.add(resetItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Панель действий
        JPanel actionPanel = createActionPanel();

        // Главная панель
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, actionPanel, createTablePanel());
        mainSplitPane.setDividerLocation(300);
        add(mainSplitPane, BorderLayout.CENTER);

        // Обновление данных
        updateAllTables();
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JButton addWoodBtn = new JButton("Добавить древесину");
        JButton addCoreBtn = new JButton("Добавить сердцевину");
        JButton addBuyerBtn = new JButton("Добавить покупателя");
        JButton addStickBtn = new JButton("Добавить палочку");
        JButton sellStickBtn = new JButton("Продать палочку");
        JButton clearAllBtn = new JButton("Очистить всё");

        panel.add(addWoodBtn);
        panel.add(addCoreBtn);
        panel.add(addBuyerBtn);
        panel.add(addStickBtn);
        panel.add(sellStickBtn);
        panel.add(clearAllBtn);

        addWoodBtn.addActionListener(e -> addWoodDialog());
        addCoreBtn.addActionListener(e -> addCoreDialog());
        addBuyerBtn.addActionListener(e -> addBuyerDialog());
        addStickBtn.addActionListener(e -> {
            try {
                addStickDialog();
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        sellStickBtn.addActionListener(e -> {
            try {
                sellStickDialog();
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        clearAllBtn.addActionListener(e -> fullReset());

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        tablePanel.add(createLabeledPanel("Древесина", woodTable));
        tablePanel.add(createLabeledPanel("Сердцевина", coreTable));
        tablePanel.add(createLabeledPanel("Палочки", stickTable));
        tablePanel.add(createLabeledPanel("Покупатели", buyerTable));
        tablePanel.add(createLabeledPanel("Продажи", saleTable));
        return tablePanel;
    }

    private JScrollPane createLabeledPanel(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    private void initTables() {
        woodModel = new DefaultTableModel(new String[]{"ID", "Название", "Количество"}, 0);
        woodTable = new JTable(woodModel);
        coreModel = new DefaultTableModel(new String[]{"ID", "Название", "Количество"}, 0);
        coreTable = new JTable(coreModel);
        stickModel = new DefaultTableModel(new String[]{"ID", "Древесина", "Сердцевина", "Количество"}, 0);
        stickTable = new JTable(stickModel);
        buyerModel = new DefaultTableModel(new String[]{"ID", "Имя", "Адрес"}, 0);
        buyerTable = new JTable(buyerModel);
        saleModel = new DefaultTableModel(new String[]{"ID", "Палочка", "Покупатель", "Дата"}, 0);
        saleTable = new JTable(saleModel);
    }

    private void updateAllTables() throws Exception {
        updateWoodTable();
        updateCoreTable();
        updateStickTable();
        updateBuyerTable();
        updateSaleTable();
    }

    private void updateWoodTable() throws Exception {
        woodModel.setRowCount(0);
        for (Wood w : shopService.getAllWoods()) {
            woodModel.addRow(new Object[]{w.getId(), w.getName(), w.getStock()});
        }
    }

    private void updateCoreTable() throws Exception {
        coreModel.setRowCount(0);
        for (Core c : shopService.getAllCores()) {
            coreModel.addRow(new Object[]{c.getId(), c.getName(), c.getStock()});
        }
    }

    private void updateStickTable() throws Exception {
        stickModel.setRowCount(0);
        for (MagicStick s : shopService.getAllSticks()) {
            stickModel.addRow(new Object[]{
                s.getId(),
                s.getWood().getName(),
                s.getCore().getName(),
                s.getStock()
            });
        }
    }

    private void updateBuyerTable() throws Exception {
        buyerModel.setRowCount(0);
        for (Buyer b : shopService.getAllBuyers()) {
            buyerModel.addRow(new Object[]{b.getId(), b.getName(), b.getAddress()});
        }
    }

    private void updateSaleTable() throws Exception {
        saleModel.setRowCount(0);
        for (Sale sale : shopService.getAllSales()) {
            MagicStick stick = shopService.getStickById(sale.getStickId());
            Buyer buyer = shopService.getBuyerById(sale.getBuyerId());
            saleModel.addRow(new Object[]{
                sale.getId(),
                stick != null ? stick.getId() : "Не найдено",
                buyer != null ? buyer.getName() : "Не найдено",
                sale.getDate() 
            });
        }
    }

    private void addWoodDialog() {
        String name = JOptionPane.showInputDialog("Название древесины:");
        if (name == null || name.trim().isEmpty()) return;
        String qtyStr = JOptionPane.showInputDialog("Количество:");
        int qty = Integer.parseInt(qtyStr);
        try {
            shopService.addWood(name, qty);
            updateWoodTable();
            JOptionPane.showMessageDialog(this, "Древесина добавлена!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void addCoreDialog() {
        String name = JOptionPane.showInputDialog("Название сердцевины:");
        if (name == null || name.trim().isEmpty()) return;
        String qtyStr = JOptionPane.showInputDialog("Количество:");
        int qty = Integer.parseInt(qtyStr);
        try {
            shopService.addCore(name, qty);
            updateCoreTable();
            JOptionPane.showMessageDialog(this, "Сердцевина добавлена!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void addBuyerDialog() {
        String name = JOptionPane.showInputDialog("Имя покупателя:");
        String address = JOptionPane.showInputDialog("Адрес:");
        try {
            shopService.addBuyer(name, address);
            updateBuyerTable();
            JOptionPane.showMessageDialog(this, "Покупатель добавлен!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void addStickDialog() throws Exception {
        String woodName = JOptionPane.showInputDialog("Древесина (название):");
        String coreName = JOptionPane.showInputDialog("Сердцевина (название):");

        int woodId = shopService.getWoodId(woodName);
        int coreId = shopService.getCoreId(coreName);

        if (woodId == -1 || coreId == -1) {
            JOptionPane.showMessageDialog(this, "Компоненты не найдены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String stockStr = JOptionPane.showInputDialog("Количество палочек:");
        int stock = Integer.parseInt(stockStr);

        try {
            shopService.addStick(woodId, coreId, stock);
            updateStickTable();
            updateComponentStock("wood", woodName, -stock);
            updateComponentStock("core", coreName, -stock);
            JOptionPane.showMessageDialog(this, "Палочки добавлены!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void sellStickDialog() throws Exception {
        int stickId = Integer.parseInt(JOptionPane.showInputDialog("ID палочки:"));
        MagicStick stick = shopService.getStickById(stickId);
        if (stick == null) {
            JOptionPane.showMessageDialog(this, "Палочка не найдена!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String buyerName = JOptionPane.showInputDialog("Имя покупателя:");
        if (buyerName == null || buyerName.trim().isEmpty()) return;

        Buyer buyer = shopService.getBuyerByName(buyerName);
        if (buyer == null) {
            String address = JOptionPane.showInputDialog("Адрес покупателя:");
            try {
                shopService.addBuyer(buyerName, address);
                buyer = shopService.getBuyerByName(buyerName);
            } catch (Exception ex) {
                showError(ex.getMessage());
                return;
            }
        }

        try {
            shopService.sellStick(stickId, buyer.getId());
            updateStickTable();
            updateSaleTable();
            updateComponentStock("wood", stick.getWood().getName(), -1);
            updateComponentStock("core", stick.getCore().getName(), -1);
            JOptionPane.showMessageDialog(this, "Палочка продана!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateComponentStock(String type, String name, int change) {
        try {
            if (type.equals("wood")) {
                Wood wood = shopService.getWoodByName(name);
                if (wood != null) {
                    wood.setStock(wood.getStock() + change);
                    shopService.updateComponentStock(wood);
                }
            } else {
                Core core = shopService.getCoreByName(name);
                if (core != null) {
                    core.setStock(core.getStock() + change);
                    shopService.updateComponentStock(core);
                }
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void fullReset() {
        try {
            shopService.fullReset();
            updateAllTables();
            JOptionPane.showMessageDialog(this, "Данные очищены!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, "Ошибка: " + message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new GUI().setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}