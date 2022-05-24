/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.model;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User implements Cloneable {
    private int id;
    private String name;
    private Date birthday;
    private String address;
    private String username;
    private String password;
    private Store store;

    public User(int id, String name, Date birthday, String address, String username, String password, Store store) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.username = username;
        this.password = password;
        this.store = store;
    }
    
    public User(String name, Date birthday, String address, String username, String password, Store store) {
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.username = username;
        this.password = password;
        this.store = store;
    }
    
    public User(String name, Date birthday, String address, Store store) {
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.store = store;
    }
    
    public User(String name, Date birthday, String address, String username, Store store) {
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.store = store;
        this.username = username;
    }
    
    public User(String name) {
        this.name = name;
    }
    
    private List<String> roleList;
    
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }
}
