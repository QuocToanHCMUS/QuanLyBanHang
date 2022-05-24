/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    public final static String prefixId = "INVOICE";
    
    private String id;
    private User user;
    private Customer customer;
    private Timestamp saleDate;
    private String products;
    private double totalAmount;
    private double discountAmount;
    private int discount;

    public Invoice(String invoiceNo, User user, Customer customer, Timestamp saleDate, double totalPrice, double discountAmount, int discount) {
        this.id = invoiceNo;
        this.user = user;
        this.customer = customer;
        this.saleDate = saleDate;
        this.totalAmount = totalPrice;
        this.discountAmount = discountAmount;
        this.discount = discount;
    }

    public Invoice(String invoiceId, Timestamp saleDate, double discountAmount) {
        this.id = invoiceId;
        this.saleDate = saleDate;
        this.discountAmount = discountAmount;
    }
}
