/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import ql.model.Customer;
/**
 *
 * @author ADMIN
 */
public class CustomerDao extends BaseDao {
    
    public String getLastCustomerId() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT TOP 1 id FROM customer ORDER BY CAST(STUFF(id, 1, 8, '') AS int) DESC ");
            ps = connection.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return null;
    }
    
    public List<Customer> search(String cusId, String cusName) {
        List<Customer> customers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM customer WHERE 1 = 1 ");
            if (cusId != null && cusId != "") {
                sql.append("AND id LIKE ? ");
            }
            if (cusName != null && cusName != "") {
                sql.append("AND name LIKE ? ");
            }
            
            ps = connection.prepareStatement(sql.toString());
            int index = 1;
            if (cusId != null && cusId != "") {
                ps.setString(index++, "%" + cusId + "%");
            }
            if (cusName != null && cusName != "") {
                ps.setString(index++, "%" + cusName + "%");
            }
            sql.append("ORDER BY CAST(STUFF(id, 1, 8, '') AS int) ASC ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customers.add(new Customer(rs.getString("id"), rs.getString("name"), rs.getDate("birthday"), rs.getString("address")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return customers;
    }
}
