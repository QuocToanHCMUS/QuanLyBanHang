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
import ql.model.SalaryStaff;
import ql.model.Staff;
import ql.model.User;

/**
 *
 * @author ADMIN
 */
public class StaffSalaryDao extends BaseDao {
    public List<SalaryStaff> search(String staffNo, int month, int year) {
        Connection connection = null;
        PreparedStatement ps = null;
        List<SalaryStaff> salaryStaffs = new ArrayList<>();
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT a.*, b.name, c.staff_no from salary_staff a INNER JOIN users b ON a.staff_id = b.id "
                    + "INNER JOIN staff c ON b.id = c.user_id INNER JOIN user_role d ON d.user_id = b.id INNER JOIN role e ON e.id = d.role_id WHERE e.id = 3 ");
            if (staffNo != null && staffNo != "") {
                sql.append(" AND c.staff_no LIKE ? ");
            }
            if (month > 0) {
                sql.append("AND MONTH(a.start_time) = ? ");
            }
            if (year > 0) {
                sql.append("AND YEAR(start_time) = ? ");
            }
            sql.append("ORDER BY created_date DESC ");
            ps = connection.prepareStatement(sql.toString());
            int index = 1;
            if (staffNo != null && staffNo != "") {
                ps.setString(index++, "%" + staffNo + "%");
            }
            if (month > 0) {
                ps.setInt(index++, month);
            }
            if (year > 0) {
                ps.setInt(index++, year);
            }
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String staffN = rs.getString("staff_no");
                User user = new Staff(staffN, name);
                Date startTime = rs.getDate("start_time");
                Date endTime = rs.getDate("end_time");
                double salary = rs.getDouble("salary");
                Timestamp createdDate = rs.getTimestamp("created_date");
                int workDay = rs.getInt("work_date");
                salaryStaffs.add(new SalaryStaff(id, user, startTime, endTime, salary, createdDate, workDay));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return salaryStaffs;
    }
}
