/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

import com.mycompany.maglshop.model.Wood;
import com.mycompany.maglshop.model.Core;

public class MagicStick {
    private int id;
    private Wood wood;
    private Core core;
    private int stock;
    private boolean sold; // Новое поле для отметки о продаже

    // Конструктор с параметром sold (для загрузки из БД)
    public MagicStick(int id, Wood wood, Core core, int stock, boolean sold) {
        this.id = id;
        this.wood = wood;
        this.core = core;
        this.stock = 1; // Всегда устанавливаем stock = 1
        this.sold = sold;
    }

    // Конструктор с параметром stock (для совместимости)
    public MagicStick(int id, Wood wood, Core core, int stock) {
        this.id = id;
        this.wood = wood;
        this.core = core;
        this.stock = 1; // Всегда устанавливаем stock = 1
        this.sold = false; // По умолчанию не продана
    }

    // Конструктор без stock (для создания новых палочек)
    public MagicStick(Wood wood, Core core) {
        this.wood = wood;
        this.core = core;
        this.stock = 1; // Всегда 1
        this.sold = false; // По умолчанию не продана
    }

    // Конструктор для создания с id, но без stock
    public MagicStick(int id, Wood wood, Core core) {
        this.id = id;
        this.wood = wood;
        this.core = core;
        this.stock = 1; // Всегда 1
        this.sold = false; // По умолчанию не продана
    }

    // Геттеры
    public int getId() { return id; }
    public Wood getWood() { return wood; }
    public Core getCore() { return core; }
    public int getStock() { return stock; }
    public boolean isSold() { return sold; }
    
    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setSold(boolean sold) { this.sold = sold; }
}
