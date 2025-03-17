/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.List;




/**
 *
 * @author pablo
 */

@Entity

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String phone;
    private String code;
    private String iban;
    private String surname;
    private String address;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerProduct> products;
    @Transient
    private List<?> transactions;

    public Customer() {
    }

    public Customer(String name, String phone,String code, String iban, String surname, String address, List<CustomerProduct> products, List<?> transactions) {
        this.name = name;
        this.phone = phone;
        this.code = code;
        this.iban = iban;
        this.surname = surname;
        this.address = address;
        this.products = products;
        this.transactions = transactions;
    }

    

    public long getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<CustomerProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CustomerProduct> products) {
        this.products = products;
    }

    public List<?> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<?> transactions) {
        this.transactions = transactions;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    
    
    

    
    
    
}
