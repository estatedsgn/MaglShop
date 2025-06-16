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
    
    private JProgressBar warehouseProgressBar;
    private JLabel warehouseLabel;
    private JPanel warehousePanel;
    
    // Константы для расчета заполненности
    private static final int MAX_WAREHOUSE_CAPACITY = 100;

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

        // Создаем основную левую панель с действиями и индикатором склада
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(actionPanel, BorderLayout.CENTER);
        leftPanel.add(createWarehousePanel(), BorderLayout.SOUTH); // Добавляем индикатор внизу

        // Главная панель
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, createTablePanel());
        mainSplitPane.setDividerLocation(300);
        add(mainSplitPane, BorderLayout.CENTER);

        // Обновление данных
        updateAllTables();
    }
 private void addWarehouseTooltip() {
    try {
        StringBuilder tooltip = new StringBuilder("<html>");
        tooltip.append("<b>Детализация склада:</b><br>");
        
        int totalWood = 0, totalCore = 0, totalSticks = 0;
        
        // Древесина
        tooltip.append("<br><u>Древесина:</u><br>");
        for (Wood wood : shopService.getAllWoods()) {
            tooltip.append(String.format("• %s: %d шт.<br>", wood.getName(), wood.getStock()));
            totalWood += wood.getStock();
        }
        
        // Сердцевины
        tooltip.append("<br><u>Сердцевины:</u><br>");
        for (Core core : shopService.getAllCores()) {
            tooltip.append(String.format("• %s: %d шт.<br>", core.getName(), core.getStock()));
            totalCore += core.getStock();
        }
        
        // Готовые палочки
        for (MagicStick stick : shopService.getAllSticks()) {
            if (!stick.isSold()) {
                totalSticks++;
            }
        }
        
        tooltip.append(String.format("<br><u>Готовые палочки:</u> %d шт.<br>", totalSticks));
        tooltip.append(String.format("<br><b>Итого:</b> %d единиц", totalWood + totalCore + totalSticks));
        
        tooltip.append("</html>");
        
        warehousePanel.setToolTipText(tooltip.toString());
        
    } catch (Exception e) {
        warehousePanel.setToolTipText("Ошибка загрузки данных склада");
    }
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
    
    private void updateWarehouseIndicator() {
       try {
           // Подсчитываем общее количество компонентов на складе
           int totalStock = 0;

           // Считаем древесину
           for (Wood wood : shopService.getAllWoods()) {
               totalStock += wood.getStock();
           }

           // Считаем сердцевины
           for (Core core : shopService.getAllCores()) {
               totalStock += core.getStock();
           }

           // Считаем непроданные палочки (каждая палочка = 1 единица)
           for (MagicStick stick : shopService.getAllSticks()) {
               if (!stick.isSold()) {
                   totalStock += 1; // Каждая палочка добавляет только 1 к общему количеству
               }
           }

           // Рассчитываем процент заполненности
           int percentage = Math.min((totalStock * 100) / MAX_WAREHOUSE_CAPACITY, 100);

           // Обновляем интерфейс
           warehouseLabel.setText(String.format("Загрузка: %d/%d (%d%%)", 
                                              totalStock, MAX_WAREHOUSE_CAPACITY, percentage));
           warehouseProgressBar.setValue(totalStock);
           warehouseProgressBar.setString(percentage + "%");

           // Изменяем цвет в зависимости от заполненности
           if (percentage <= 60) {
               warehouseProgressBar.setForeground(Color.GREEN);
           } else if (percentage <= 80) {
               warehouseProgressBar.setForeground(Color.ORANGE);
           } else {
               warehouseProgressBar.setForeground(Color.RED);
           }

           // Обновляем максимальное значение прогресс-бара, если нужно
           if (totalStock > MAX_WAREHOUSE_CAPACITY) {
               warehouseProgressBar.setMaximum(totalStock);
           }

       } catch (Exception e) {
           warehouseLabel.setText("Ошибка загрузки данных");
           warehouseProgressBar.setValue(0);
           warehouseProgressBar.setString("0%");
       }
   }
    
            private JPanel createWarehousePanel() {
        warehousePanel = new JPanel();
        warehousePanel.setLayout(new BoxLayout(warehousePanel, BoxLayout.Y_AXIS));
        warehousePanel.setBorder(BorderFactory.createTitledBorder("Заполненность склада"));
        warehousePanel.setPreferredSize(new Dimension(280, 100)); // Уменьшили высоту

        // Заголовок
        warehouseLabel = new JLabel("Загрузка: 0/100 (0%)", SwingConstants.CENTER);
        warehouseLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // Прогресс-бар
        warehouseProgressBar = new JProgressBar(0, MAX_WAREHOUSE_CAPACITY);
        warehouseProgressBar.setStringPainted(true);
        warehouseProgressBar.setString("0%");

        // Цветовая схема для прогресс-бара
        warehouseProgressBar.setForeground(Color.GREEN);

        // Убираем легенду или делаем её более компактной
        JPanel legendPanel = new JPanel(new FlowLayout());
        JLabel legendLabel = new JLabel("");
        legendPanel.add(legendLabel);

        warehousePanel.add(Box.createVerticalStrut(5));
        warehousePanel.add(warehouseLabel);
        warehousePanel.add(Box.createVerticalStrut(5));
        warehousePanel.add(warehouseProgressBar);
        warehousePanel.add(Box.createVerticalStrut(3));
        warehousePanel.add(legendPanel);

        return warehousePanel;
    }


    private void updateAllTables() throws Exception {
        updateWoodTable();
        updateCoreTable();
        updateStickTable();
        updateBuyerTable();
        updateSaleTable();
        updateWarehouseIndicator();
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
            int row = stickModel.getRowCount();
            stickModel.addRow(new Object[]{
                s.getId(),
                s.getWood().getName(),
                s.getCore().getName(),
                s.getStock()
            });

            // Если палочка продана, помечаем строку красным цветом
            if (s.isSold()) {
                // Нужно создать кастомный рендерер для таблицы
            }
        }
    }

    // Добавить кастомный рендерер в метод initTables:
    private void initTables() {
        woodModel = new DefaultTableModel(new String[]{"ID", "Название", "Количество"}, 0);
        woodTable = new JTable(woodModel);
        coreModel = new DefaultTableModel(new String[]{"ID", "Название", "Количество"}, 0);
        coreTable = new JTable(coreModel);
        stickModel = new DefaultTableModel(new String[]{"ID", "Древесина", "Сердцевина", "Количество"}, 0);
        stickTable = new JTable(stickModel);

        // Кастомный рендерер для таблицы палочек
        stickTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    int stickId = (Integer) table.getValueAt(row, 0);
                    MagicStick stick = shopService.getStickById(stickId);
                    if (stick != null && stick.isSold()) {
                        c.setBackground(Color.RED);
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                        c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                    }
                } catch (Exception e) {
                    // В случае ошибки используем стандартные цвета
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }

                return c;
            }
        });

        buyerModel = new DefaultTableModel(new String[]{"ID", "Имя", "Адрес"}, 0);
        buyerTable = new JTable(buyerModel);
        saleModel = new DefaultTableModel(new String[]{"ID", "ID Палочки", "Покупатель", "Дата"}, 0);
        saleTable = new JTable(saleModel);
    }

    // Изменить метод updateSaleTable:
    private void updateSaleTable() throws Exception {
        saleModel.setRowCount(0);
        for (Sale sale : shopService.getAllSales()) {
            Buyer buyer = shopService.getBuyerById(sale.getBuyerId());
            saleModel.addRow(new Object[]{
                sale.getId(),
                sale.getStickId(), // Теперь показываем ID палочки
                buyer != null ? buyer.getName() : "Не найдено",
                sale.getDate() 
            });
        }
    }
    // Изменить метод sellStickDialog для проверки доступности:


    private void updateBuyerTable() throws Exception {
        buyerModel.setRowCount(0);
        for (Buyer b : shopService.getAllBuyers()) {
            buyerModel.addRow(new Object[]{b.getId(), b.getName(), b.getAddress()});
        }
    }




private void addWoodDialog() {
    String name = JOptionPane.showInputDialog("Название древесины:");
    if (name == null || name.trim().isEmpty()) return;
    
    String qtyStr = JOptionPane.showInputDialog("Количество:");
    if (qtyStr == null || qtyStr.trim().isEmpty()) return;
    
    try {
        int qty = Integer.parseInt(qtyStr);
        
        // Проверяем на отрицательное число
        if (qty < 0) {
            JOptionPane.showMessageDialog(this, "Количество не может быть отрицательным!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        shopService.addWood(name, qty);
        updateWoodTable();
        updateWarehouseIndicator();
        JOptionPane.showMessageDialog(this, "Древесина добавлена!");
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Введите корректное число!", "Ошибка", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        showError(ex.getMessage());
    }
}

private void addCoreDialog() {
    String name = JOptionPane.showInputDialog("Название сердцевины:");
    if (name == null || name.trim().isEmpty()) return;
    
    String qtyStr = JOptionPane.showInputDialog("Количество:");
    if (qtyStr == null || qtyStr.trim().isEmpty()) return;
    
    try {
        int qty = Integer.parseInt(qtyStr);
        
        // Проверяем на отрицательное число
        if (qty < 0) {
            JOptionPane.showMessageDialog(this, "Количество не может быть отрицательным!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        shopService.addCore(name, qty);
        updateCoreTable();
        updateWarehouseIndicator();
        JOptionPane.showMessageDialog(this, "Сердцевина добавлена!");
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Введите корректное число!", "Ошибка", JOptionPane.ERROR_MESSAGE);
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
        if (woodName == null || woodName.trim().isEmpty()) return;

        String coreName = JOptionPane.showInputDialog("Сердцевина (название):");
        if (coreName == null || coreName.trim().isEmpty()) return;

        try {
            // Получаем или создаем компоненты
            Wood wood = shopService.getWoodByName(woodName);
            if (wood == null) {
                String qtyStr = JOptionPane.showInputDialog("Древесина не найдена. Добавить на склад? Количество:");
                if (qtyStr == null) return;
                int qty = Integer.parseInt(qtyStr);
                shopService.addWood(woodName, qty);
                wood = shopService.getWoodByName(woodName);
            }

            Core core = shopService.getCoreByName(coreName);
            if (core == null) {
                String qtyStr = JOptionPane.showInputDialog("Сердцевина не найдена. Добавить на склад? Количество:");
                if (qtyStr == null) return;
                int qty = Integer.parseInt(qtyStr);
                shopService.addCore(coreName, qty);
                core = shopService.getCoreByName(coreName);
            }

            // Проверяем, не существует ли уже такая палочка
            MagicStick existing = shopService.getStickByWoodAndCore(wood.getId(), core.getId());
            if (existing != null) {
                JOptionPane.showMessageDialog(this, "Палочка с такой комбинацией уже существует!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            shopService.addStick(wood.getId(), core.getId());
            updateAllTables();
            JOptionPane.showMessageDialog(this, "Палочка добавлена!");
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
            if (address == null || address.trim().isEmpty()) return;
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
            updateAllTables();
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


}