/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.billing.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 *
 * @author pablo
 */
@Entity
@Schema(name = "Invoice, factura", description = "modelo de factura en la base de datos")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Schema(name = "customerId", description = "Valor que representa al cliente", requiredMode = Schema.RequiredMode.REQUIRED, example = "2", defaultValue = "1")
    private long customerId;
    private String number;
    private String detail;
    private double amount;

    public long getCustomerId() {
        return customerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Invoice() {
    }

    public Invoice(Long customerId, String number, String detail, double amount) {
        this.customerId = customerId;
        this.number = number;
        this.detail = detail;
        this.amount = amount;
    }

}
