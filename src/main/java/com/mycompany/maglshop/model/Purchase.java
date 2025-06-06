/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

/**
 *
 * @author nyaku
 */

public class Purchase {
    private int id;
    private int buyerId;
    private int palochkaId;
    private String date;

    public Purchase(int id, int buyerId, int palochkaId, String date) {
        this.id = id;
        this.buyerId = buyerId;
        this.palochkaId = palochkaId;
        this.date = date;
    }

    public int getId() { return id; }
    public int getBuyerId() { return buyerId; }
    public int getPalochkaId() { return palochkaId; }
    public String getDate() { return date; }
}
