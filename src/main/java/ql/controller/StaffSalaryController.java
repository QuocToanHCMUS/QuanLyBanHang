/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.util.List;
import ql.dao.StaffSalaryDao;
import ql.model.SalaryStaff;

/**
 *
 * @author ADMIN
 */
public class StaffSalaryController {
    private StaffSalaryDao staffSalaryDao;
    
    public StaffSalaryController() {
        this.staffSalaryDao = new StaffSalaryDao();
    }
    
    public List<SalaryStaff> search(String staffNo, int month, int year) {
        month = year > 0 ? month + 1 : month;
        return staffSalaryDao.search(staffNo, month, year);
    }
}
