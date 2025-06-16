/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maglshop.model;

/**
 *
 * @author nyaku
 */


import com.mycompany.maglshop.dao.*;
import com.mycompany.maglshop.model.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShopService {
    private final WoodDAO woodDAO;
    private final CoreDAO coreDAO;
    private final MagicStickDAO stickDAO;
    private final BuyerDAO buyerDAO;
    private final SaleDAO saleDAO;
    private final DeliveryDAO deliveryDAO;

    public ShopService() {
        this.woodDAO = new WoodDAO();
        this.coreDAO = new CoreDAO();
        this.stickDAO = new MagicStickDAO();
        this.buyerDAO = new BuyerDAO();
        this.saleDAO = new SaleDAO();
        this.deliveryDAO = new DeliveryDAO();
    }

    public List<Wood> getAllWoods() throws Exception {
        return woodDAO.getAll();
    }

    public List<Core> getAllCores() throws Exception {
        return coreDAO.getAll();
    }

    public List<MagicStick> getAllSticks() throws Exception {
        return stickDAO.getAll();
    }

    public List<Buyer> getAllBuyers() throws Exception {
        return buyerDAO.getAll();
    }

    public List<Sale> getAllSales() throws Exception {
        return saleDAO.getAll();
    }

    public List<Delivery> getAllDeliveries() throws Exception {
        return deliveryDAO.getAll();
    }

    public int getWoodId(String name) throws Exception {
        return woodDAO.getId(name);
    }

    public int getCoreId(String name) throws Exception {
        return coreDAO.getId(name);
    }

    public Wood getWoodById(int id) throws Exception {
        return woodDAO.getById(id);
    }

    public Core getCoreById(int id) throws Exception {
        return coreDAO.getById(id);
    }

    public Wood getWoodByName(String name) throws Exception {
        return woodDAO.getByName(name);
    }

    public Core getCoreByName(String name) throws Exception {
        return coreDAO.getByName(name);
    }

    public MagicStick getStickById(int id) throws Exception {
        return stickDAO.getById(id);
    }

    public Buyer getBuyerById(int id) throws Exception {
        return buyerDAO.getById(id);
    }

    public Buyer getBuyerByName(String name) throws Exception {
        return buyerDAO.getByName(name);
    }

    public void addWood(String name, int stock) throws Exception {
        woodDAO.addComponent(new Wood(0, name, stock));
    }

    public void addCore(String name, int stock) throws Exception {
        coreDAO.addComponent(new Core(0, name, stock));
    }

    public void addBuyer(String name, String address) throws Exception {
        buyerDAO.addBuyer(new Buyer(0, name, address));
    }

    public void addDelivery(String type, String name, int quantity, String date) throws Exception {
        int componentId = type.equals("wood") ? getWoodId(name) : getCoreId(name);
        deliveryDAO.addDelivery(new Delivery(0, componentId, quantity, date));
    }
    public MagicStick getStickByWoodAndCore(int woodId, int coreId) throws Exception {
        return stickDAO.getByWoodAndCore(woodId, coreId);
    }
    public void addStick(int woodId, int coreId) throws Exception {
        Wood wood = woodDAO.getById(woodId);
        Core core = coreDAO.getById(coreId);

        if (wood == null) throw new Exception("Древесина с ID " + woodId + " не найдена");
        if (core == null) throw new Exception("Сердцевина с ID " + coreId + " не найдена");

        // Проверяем наличие компонентов на складе
        if (wood.getStock() < 1) throw new Exception("Недостаточно древесины на складе");
        if (core.getStock() < 1) throw new Exception("Недостаточно сердцевины на складе");

        // Создаем палочку (всегда с количеством 1)
        MagicStick stick = new MagicStick(wood, core);
        stickDAO.addStick(stick);

        // Уменьшаем количество компонентов на складе
        wood.setStock(wood.getStock() - 1);
        core.setStock(core.getStock() - 1);
        woodDAO.updateStock(wood);
        coreDAO.updateStock(core);
    }

    // Изменить метод sellStick:
    public void sellStick(int stickId, int buyerId) throws Exception {
        MagicStick stick = getStickById(stickId);
        if (stick == null) throw new Exception("Палочка не найдена");
        if (stick.isSold()) throw new Exception("Палочка уже продана");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(new java.util.Date());

        Sale sale = new Sale(0, stickId, buyerId, formattedDate);
        saleDAO.addSale(sale);

        // Помечаем палочку как проданную
        stickDAO.markAsSold(stickId);
    }

    // Добавить метод для получения доступных (непроданных) палочек:
    public List<MagicStick> getAvailableSticks() throws Exception {
        List<MagicStick> allSticks = stickDAO.getAll();
        List<MagicStick> availableSticks = new ArrayList<>();
        for (MagicStick stick : allSticks) {
            if (!stick.isSold()) {
                availableSticks.add(stick);
            }
        }
        return availableSticks;
    }
    public void updateComponentStock(Wood wood) throws Exception {
        woodDAO.updateStock(wood);
    }

    public void updateComponentStock(Core core) throws Exception {
        coreDAO.updateStock(core);
    }

    public void fullReset() throws Exception {
        saleDAO.deleteAll();
        deliveryDAO.deleteAll();
        buyerDAO.deleteAll();
        stickDAO.deleteAll();
        coreDAO.deleteAll();
        woodDAO.deleteAll();
    }
}