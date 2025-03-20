/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transactions.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

/**
 *
 * @author pablo
 */
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String reference;
    private String accountIban;
    private LocalDateTime date;
    private double amount;
    private double fee;
    private String description;
    private Status status;
    private Channel channel;

    public Transaction() {
    }

    public Transaction(String reference, String accountIban, LocalDateTime date, double amount, double fee, String description, Status status, Channel channel) {
        this.reference = reference;
        this.accountIban = accountIban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.description = description;
        this.status = status;
        this.channel = channel;
        validar();
    }
    
    private void validar(){
        if(fee>0)
            amount -= fee;
        
        if(amount - amount ==0)
            throw new IllegalArgumentException("No puede ser 0");
        
        if(date.isAfter(LocalDateTime.now())){
            status = Status.PENDIENTE;
        }else{
            status = Status.LIQUIDADA;
        }
    }

    public long getId() {
        return id;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAccountIban() {
        return accountIban;
    }

    public void setAccountIban(String accountIban) {
        this.accountIban = accountIban;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    
    
    
    
    public enum Status{
        PENDIENTE("01"),
        LIQUIDADA("02"),
        RECHAZADA("03"),
        CANCELADA("04");
        
        private final String code;

        private Status(String code) {
            this.code = code;
        }
        
        public String getCode(){
            return code;
        }
        
    }
    
    public enum Channel{
        WEB, 
        CAJERO,
        OFICINA;

    }
}
