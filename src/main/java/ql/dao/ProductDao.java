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
import ql.model.DetailProduct;
import ql.model.Product;

/**
 *
 * @author ADMIN
 */
public class ProductDao extends BaseDao {

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM product ");
            ps = connection.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(new Product(rs.getString("id"), rs.getString("name"), rs.getDouble("price")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return products;
    }

    public List<Product> search(String storeId, String id, String name) {
        List<Product> products = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT a.id, a.name, a.price, a.import_price, b.quantity, b.sell_quantity FROM product a INNER JOIN detail_stock b ON a.id = b.product_id WHERE b.store_id = ? ");
            if (name != null && name != "") {
                sql.append(" AND a.name LIKE ?");
            }
            if (id != null && id != "") {
                sql.append(" AND a.id LIKE ?");
            }
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, storeId);
            int index = 1;
            if (name != null && name != "") {
                ps.setString(index++, "%" + name + "%");
            }
            if (id != null && id != "") {
                ps.setString(index++, "%" + id + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetailProduct detailProduct = new DetailProduct(rs.getString("id"), storeId, rs.getInt("quantity"), rs.getInt("sell_quantity"));
                products.add(new Product(rs.getString("id"), rs.getString("name"), rs.getDouble("price"), rs.getDouble("import_price"), detailProduct));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return products;
    }
    
    public boolean insert(Product product) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("INSERT INTO product(id, name, price, import_price) VALUES (?, ?, ?, ?)");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, product.getId());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getImportPrice() + 2.0);
            ps.setDouble(4, product.getImportPrice());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }

    public boolean update(String id, String name, double salePrice) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("UPDATE product SET name = ?, price = ? WHERE id = ?");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, name);
            ps.setDouble(2, salePrice);
            ps.setString(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }

    public boolean remove(String storeId, String productId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            StringBuilder sql = new StringBuilder("DELETE FROM detail_stock WHERE store_id = ? AND product_id = ?");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, storeId);
            ps.setString(2, productId);
            ps.executeUpdate();
            
            sql = new StringBuilder("DELETE FROM product WHERE id = ?");
            ps.setString(1, productId);
            ps.executeUpdate();
            
            connection.commit();
            return true;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }
    
    public boolean checkExistProduct(String productId, String storeId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT a.id FROM product a INNER JOIN detail_stock b ON a.id = b.product_id INNER JOIN store c ON c.id = b.store_id WHERE c.id = ? AND a.id = ?");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, storeId);
            ps.setString(2, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }
}
