/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.product.controller;

import com.paymentchain.product.entities.Product;
import com.paymentchain.product.exception.BusinessRuleException;
import com.paymentchain.product.repository.ProductRepository;
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

/**
 *
 * @author pablo
 */
@RestController
@RequestMapping("/product")
public class ProductRestController {
    
    @Autowired
    private ProductRepository productRepository;
    
   
    
    @GetMapping()
    public List<Product> list() {
        return productRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable("id") long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> put(@PathVariable("id") long id, @RequestBody Product input) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            Product newProduct = product.get();
            newProduct.setName(input.getName());
            newProduct.setCode(input.getCode());
            Product save =productRepository.save(newProduct);
            return new ResponseEntity<>(save, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping
    public ResponseEntity<Product> post(@RequestBody Product input) throws BusinessRuleException {
        if(input.getCode().isBlank() || input.getName().isBlank()){
            throw new BusinessRuleException("1023", HttpStatus.PRECONDITION_FAILED, "El codigo y el nombre no pueden ser blancos o nulos");
        }else{
            productRepository.save(input);
            return new ResponseEntity<>(input, HttpStatus.OK);
        }
        
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> delete(@PathVariable("id") long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        
    }
    
    
    
}
