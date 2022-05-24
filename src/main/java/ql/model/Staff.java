/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Staff extends User implements Cloneable {

    public static final String prefixId = "NV";
    
    private String staffNo;
    private int rank;

    public Staff(String staffNo, int rank, int id, String name, Date birthday, String address, String username, String password, Store store) {
        super(id, name, birthday, address, username, password, store);
        this.staffNo = staffNo;
        this.rank = rank;
    }

    public Staff() {

    }

    public Staff(String staffNo, int rank, String name, Date birthday, String address, Store store) {
        super(name, birthday, address, store);
        this.staffNo = staffNo;
        this.rank = rank;
    }

    public Staff(String staffN, String name) {
        super(name);
        this.staffNo = staffN;
    }
    
    public Staff clone() throws CloneNotSupportedException {
        return (Staff) super.clone();
    }
}
