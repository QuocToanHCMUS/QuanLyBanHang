/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ql.model.Store;

/**
 *
 * @author ADMIN
 */
public class StoreDao extends BaseDao {
    
    public int getQuantity(String storeId, String productId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT quantity FROM detail_stock WHERE store_id = ? AND product_id = ?");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, storeId);
            ps.setString(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return 0;
    }

    public List<Store> findAll() {
        Connection connection = null;
        PreparedStatement ps = null;
        List<Store> storeList = new ArrayList<>();
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM store");
            ps = connection.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                storeList.add(new Store(rs.getString("id"), rs.getString("name"), rs.getString("address")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return storeList;
    }
    
    public boolean updateQuantity(String productId, String storeId, int quantity, double importProductPrice) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            StringBuilder sql = new StringBuilder("SELECT quantity FROM detail_stock WHERE store_id = ? AND product_id = ?");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, storeId);
            ps.setString(2, productId);
            ResultSet rs = ps.executeQuery();
            int currentQuantity = 0;
            if (rs.next()) {
                currentQuantity = rs.getInt("quantity");
            }
            
            sql = new StringBuilder("UPDATE detail_stock SET quantity = ? WHERE store_id = ? AND product_id = ?");
            ps = connection.prepareStatement(sql.toString());
            ps.setInt(1, currentQuantity + quantity);
            ps.setString(2, storeId);
            ps.setString(3, productId);
            ps.executeUpdate();
            
            sql = new StringBuilder("UPDATE product SET import_price = ? WHERE product_id = ?");
            ps = connection.prepareStatement(sql.toString());
            ps.setDouble(1, importProductPrice);
            ps.setString(2, productId);
            ps.executeUpdate();
            
            connection.commit();
            return true;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(StoreDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }
    
    public boolean insertQuantity(String productId, String storeId, int quantity) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("INSERT detail_stock(store_id, product_id, quantity, sell_quantity) VALUES(?, ?, ?, ?)");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, storeId);
            ps.setString(2, productId);
            ps.setInt(3, quantity);
            ps.setInt(4, 0);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }
}
