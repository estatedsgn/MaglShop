/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

/**
 *
 * @author nyaku
 */

import com.mycompany.maglshop.model.Wood;
import com.mycompany.maglshop.model.Core;

public class MagicStick {
    private int id;
    private Wood wood;
    private Core core;
    private int stock;

    public MagicStick(int id, Wood wood, Core core, int stock) {
        this.id = id;
        this.wood = wood;
        this.core = core;
        this.stock = stock;
    }

    // Геттеры
    public int getId() { return id; }
    public Wood getWood() { return wood; }
    public Core getCore() { return core; }
    public int getStock() { return stock; }
}
