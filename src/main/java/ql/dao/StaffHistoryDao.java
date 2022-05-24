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
import java.util.List;
import ql.model.StaffHistory;

/**
 *
 * @author ADMIN
 */
public class StaffHistoryDao extends BaseDao {

    public boolean insert(int staffId, String description) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("INSERT INTO history_staff(staff_id, description, created_date) VALUES(?, ?, ?)");
            ps = connection.prepareStatement(sql.toString());
            ps.setInt(1, staffId);
            ps.setString(2, description);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }

    public List<StaffHistory> findAllBy(String staffNo) {
        List<StaffHistory> staffHistorys = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT a.* FROM history_staff a INNER JOIN users b ON a.staff_id = b.id INNER JOIN staff c ON b.id = c.user_id WHERE c.staff_no = ?");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, staffNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                staffHistorys.add(new StaffHistory(rs.getInt("id"), rs.getInt("staff_id"), rs.getString("description"), rs.getTimestamp("created_date")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return staffHistorys;
    }
    
}
