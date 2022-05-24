/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ADMIN
 */
@Getter
@Setter
@AllArgsConstructor
public class Store {
    private String id;
    private String name;
    private String address;

    public Store() {
    }
}
