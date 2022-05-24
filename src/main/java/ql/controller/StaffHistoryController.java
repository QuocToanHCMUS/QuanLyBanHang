/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.util.List;
import ql.dao.StaffHistoryDao;
import ql.model.StaffHistory;

/**
 *
 * @author ADMIN
 */
public class StaffHistoryController {
    private StaffHistoryDao staffHistoryDao;
    
    public StaffHistoryController() {
        staffHistoryDao = new StaffHistoryDao();
    }
    
    public boolean insert(int staffId, String description) {
        return staffHistoryDao.insert(staffId, description);
    }

    public List<StaffHistory> findAllBy(String staffNo) {
        return staffHistoryDao.findAllBy(staffNo);
    }
}
