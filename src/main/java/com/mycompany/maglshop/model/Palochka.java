/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

public class Palochka {
    private int id;
    private int woodId;
    private int coreId;

    public Palochka(int id, int woodId, int coreId) {
        this.id = id;
        this.woodId = woodId;
        this.coreId = coreId;
    }

    public int getId() { return id; }
    public int getWoodId() { return woodId; }
    public int getCoreId() { return coreId; }
}