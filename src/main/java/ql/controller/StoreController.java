/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.util.List;
import ql.dao.StoreDao;
import ql.model.Store;

/**
 *
 * @author ADMIN
 */
public class StoreController {
    private StoreDao storeDao;
    
    public StoreController() {
        storeDao = new StoreDao();
    }
    
    public int getQuantity(String storeId, String productId) {
        return storeDao.getQuantity(storeId, productId);
    }
    
    public List<Store> findAll() {
        return storeDao.findAll();
    }
}
