/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import ql.common.Utils;
import ql.model.WorkTime;


public class WorkTimeDao extends BaseDao {
    public List<WorkTime> getWorkTime() {
        List<WorkTime> workTimeList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM work_time");
            ps = connection.prepareCall(sql.toString());
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    workTimeList.add(new WorkTime(rs.getInt("id"), rs.getInt("type"), rs.getTime("start_time"), rs.getTime("end_time"), rs.getString("description")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return workTimeList;
    }
    
}
