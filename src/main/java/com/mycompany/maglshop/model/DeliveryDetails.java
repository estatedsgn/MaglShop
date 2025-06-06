/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

/**
 *
 * @author nyaku
 */

public class DeliveryDetails {
    private int id;
    private int deliveryId;
    private String componentType;
    private String componentName;
    private int quantity;

    public DeliveryDetails(int id, int deliveryId, String componentType, String componentName, int quantity) {
        this.id = id;
        this.deliveryId = deliveryId;
        this.componentType = componentType;
        this.componentName = componentName;
        this.quantity = quantity;
    }

    // Геттеры
    public int getId() { return id; }
    public int getDeliveryId() { return deliveryId; }
    public String getComponentType() { return componentType; }
    public String getComponentName() { return componentName; }
    public int getQuantity() { return quantity; }
}