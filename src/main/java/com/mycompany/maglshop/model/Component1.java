/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;


public class Component1 {
    private int id;
    private String name;
    private String type; // wood/core
    private int stockQuantity;

    public Component1(int id, String name, String type, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.stockQuantity = stockQuantity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getStockQuantity() { return stockQuantity; }
}