/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.transactions.controller;

import com.paymentchain.transactions.entities.Transaction;
import com.paymentchain.transactions.repository.TransactionRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author pablo
 */
@RestController
@RequestMapping("/transaction")
public class TransactionRestController {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @GetMapping()
    public List<Transaction> list() {
        List<Transaction> transactions = transactionRepository.findAll();
        Double saldo = 0.0;
        for(Transaction trans : transactions){
            saldo += trans.getAmount();
        }
        return transactions;
    }
    
    @GetMapping("customer/transactions")
    public Transaction getByIban(@RequestParam("iban") String accountIban) {
        Transaction transaction = transactionRepository.findByIban(accountIban);
        Double saldo = 0.0;
        saldo = transaction.getAmount();
        return transaction;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable("id") long id, @RequestBody Transaction input) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if(transaction.isPresent()){
            Transaction newTransaction = transaction.get();
            newTransaction.setAccountIban(input.getAccountIban());
            newTransaction.setAmount(input.getAmount());
            newTransaction.setChannel(input.getChannel());
            newTransaction.setDate(input.getDate());
            newTransaction.setDescription(input.getDescription());
            newTransaction.setFee(input.getFee());
            newTransaction.setReference(input.getReference());
            newTransaction.setStatus(input.getStatus());
            Transaction save = transactionRepository.save(newTransaction);
            return new ResponseEntity<>(save, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Transaction input) {
        Transaction save = transactionRepository.save(input);
        return ResponseEntity.ok(save);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if(transaction.isPresent()){
            transactionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
}
