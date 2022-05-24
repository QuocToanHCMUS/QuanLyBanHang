/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.model;

import java.util.Date;


public class StoreManager extends User {

    public StoreManager(int id, String name, Date birthday, String address, String username, String password, Store store) {
        super(id, name, birthday, address, username, password, store);
    }

    public StoreManager() {
    }

    public StoreManager(String name, Date birthday, String address, String username, Store store) {
        super(name, birthday, address, username, store);
    }

    public StoreManager(String name, Date birthday, String address, String username, String password, Store store) {
        super(name, birthday, address, username, password, store);
    }
    
}
