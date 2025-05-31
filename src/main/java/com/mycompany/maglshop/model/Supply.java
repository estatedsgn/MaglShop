/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

public class Supply {
    private int id;
    private int componentId;
    private int quantity;
    private String date;

    public Supply(int id, int componentId, int quantity, String date) {
        this.id = id;
        this.componentId = componentId;
        this.quantity = quantity;
        this.date = date;
    }

    public int getId() { return id; }
    public int getComponentId() { return componentId; }
    public int getQuantity() { return quantity; }
    public String getDate() { return date; }
}