/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ql.common.SecurityUtil;
import ql.common.Utils;
import ql.dao.UserDao;
import ql.model.Staff;
import ql.model.Store;
import ql.model.StoreManager;
import ql.model.User;

/**
 *
 * @author ADMIN
 */
public class UserController {
    
    private UserDao userDao;

    public UserController() {
        userDao = new UserDao();
    }
    public User login(String username, String password) {
        User user = userDao.getUserByUsername(username);
        if (user != null) {
            System.out.println("Login: " + user.toString());
            String requestHash = SecurityUtil.getMD5(username + password);
            if (!requestHash.equals(user.getPassword())) {
                System.out.println("Login fail");
                return null;
            }
            System.out.println("Login success");
        }
        return user;
    }
    
    public List<User> getAllStaff(String storeId, String name) {
        return userDao.getAllStaff(storeId, name);
    }
    
    public String getNextStaffNo() {
        String lastStaffId = userDao.getLastStaffId();
        if (lastStaffId == null) {
            return Staff.prefixId + "1";
        }
        
        Matcher matcher = Pattern.compile("\\d+").matcher(lastStaffId);
        if (!matcher.find()) {
            return Staff.prefixId + "1";
        }
        int lastId = Integer.parseInt(matcher.group());
        return Staff.prefixId + (lastId + 1);
    }

    public boolean add(String staffNo, String username, String password, String name, Date birthday, String address, int rank, String storeId) {
        Store store = new Store();
        store.setId(storeId);
        String encryptPassword = SecurityUtil.getMD5(username + password);
        User user = new Staff(staffNo, rank, rank, name, birthday, address, username, encryptPassword, store);
        return userDao.add(user);
    }

    public boolean update(String staffNo, String name, Date birthday, String address, int rank, Store store) {
        User user = new Staff(staffNo, rank, name, birthday, address, store);
        return userDao.update(user);
    }

    public boolean remove(String staffNo) {
        return userDao.remove(staffNo);
    }
    
    public List<User> getAllStoreManagerBy(String storeId, String name) {
        return userDao.getAllStoreManagerBy(storeId, name);
    }

    public boolean add(String username, String password, String name, Date birthday, String address, String storeId) {
        Store store = new Store();
        store.setId(storeId);
        String encryptPassword = SecurityUtil.getMD5(username + password);
        User user = new StoreManager(name, birthday, address, username, password, store);
        return userDao.addStoreManager(user);
    }

    public boolean update(String username, String name, Date birthday, String address, String storeId) {
        Store store = new Store();
        store.setId(storeId);
        User user = new StoreManager(name, birthday, address, username, store);
        return userDao.updateStoreManager(user);
    }

    public boolean removeStoreManager(String username) {
        return userDao.removeStoreManager(username);
    }
}
