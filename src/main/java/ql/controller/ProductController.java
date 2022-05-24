/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.util.List;
import ql.dao.ProductDao;
import ql.model.Product;

/**
 *
 * @author ADMIN
 */
public class ProductController {
    private ProductDao productDao;
    
    public ProductController() {
        productDao = new ProductDao();
    }
    
    public List<Product> findAll() {
        return productDao.findAll();
    }

    public List<Product> search(String storeId, String id, String name) {
        return productDao.search(storeId, id, name);
    }

    public boolean edit(String id, String name, double salePrice) {
        return productDao.update(id, name, salePrice);
    }

    public boolean remove(String storeId, String productId) {
        return productDao.remove(storeId, productId);
    }
}
