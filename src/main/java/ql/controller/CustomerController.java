/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.util.List;
import ql.dao.CustomerDao;
import ql.model.Customer;

/**
 *
 * @author ADMIN
 */
public class CustomerController {
    private CustomerDao customerDao;
    
    public CustomerController() {
        customerDao = new CustomerDao();
    }
    
    public List<Customer> search(String cusId, String cusName) {
        return customerDao.search(cusId, cusName);
    }
}
