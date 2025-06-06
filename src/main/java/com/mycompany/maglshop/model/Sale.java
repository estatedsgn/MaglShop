/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

/**
 *
 * @author nyaku
 */




import java.text.SimpleDateFormat;
import java.util.Date;

public class Sale {
    private int id;
    private int stickId;
    private int buyerId;
    private String date;

    public Sale(int id, int stickId, int buyerId, String date) {
        this.id = id;
        this.stickId = stickId;
        this.buyerId = buyerId;
        this.date = date;
    }

    // Геттеры
    public int getId() { return id; }
    public int getStickId() { return stickId; }
    public int getBuyerId() { return buyerId; }
    public String getDate() { return date; }
    
    public String getFormattedDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(date);
}
}
