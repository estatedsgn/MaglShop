/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

/**
 *
 * @author nyaku
 */

public class Delivery {
    private int id;
    private String componentType;
    private int componentId;
    private int quantity;
    private String date;

    public Delivery(int id, int componentId, int quantity, String componentType) {
        this.id = id;
        this.componentType = componentType;
        this.componentId = componentId;
        this.quantity = quantity;
        this.date = date;
    }

    // Геттеры
    public int getId() { return id; }
    public String getComponentType() { return componentType; }
    public int getComponentId() { return componentId; }
    public int getQuantity() { return quantity; }
    public String getDate() { return date; }
}