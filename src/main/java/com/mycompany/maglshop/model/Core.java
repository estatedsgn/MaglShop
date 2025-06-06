/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

/**
 *
 * @author nyaku
 */

public class Core {
    private int id;
    private String name;
    private int stock;

    public Core(int id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
