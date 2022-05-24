/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ql.model.DetailImportInvoice;
import ql.model.ImportInvoice;
import ql.model.Product;

/**
 *
 * @author ADMIN
 */
public class ImportInvoiceDao extends BaseDao {
    public List<DetailImportInvoice> search(String storeId, int userId, String id) {
        List<DetailImportInvoice> invoices = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT a.id as invoiceId, "
                    + "a.import_invoice_date, d.id as productId, d.name as productName, d.price as productPrice, a.total_price, c.quantity, c.price FROM import_invoice a ");
            sql.append("INNER JOIN users b ON a.store_manager_id = b.id ");
            sql.append("INNER JOIN detail_import_invoice c ON a.id = c.import_invoice_id ");
            sql.append("INNER JOIN product d ON c.product_id = d.id ");
            sql.append("INNER JOIN user_role e ON b.id = e.user_id ");
            sql.append("INNER JOIN role f ON f.id = e.role_id ");
            sql.append("WHERE f.id = 2 AND b.id = ? AND b.store_id = ? ");
            if (id != null && id != "") {
                sql.append("AND a.id LIKE ? ");
            }
            sql.append("GROUP BY a.id, a.import_invoice_date, d.id, d.name, d.price, a.total_price, c.quantity, c.price ");
            sql.append("ORDER BY a.import_invoice_date DESC, a.id ASC");
            ps = connection.prepareCall(sql.toString());
            ps.setInt(1, userId);
            ps.setString(2, storeId);
            if (id != null && id != "") {
                ps.setString(3, '%' + id + '%');
            }
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    String invoiceId = rs.getString("invoiceId");
                    Date saleDate = rs.getDate("import_invoice_date");
                    double totalPrice = rs.getDouble("total_price");
                    ImportInvoice importInvoice = new ImportInvoice(invoiceId, saleDate, totalPrice);
                    
                    String productId = rs.getString("productId");
                    String productName = rs.getString("productName");
                    double productPrice = rs.getDouble("productPrice");
                    Product product = new Product(productId, productName, productPrice);
                    
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    invoices.add(new DetailImportInvoice(importInvoice, product, quantity, price));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return invoices;
    }
}
