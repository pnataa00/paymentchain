/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.customer.controller;

import com.paymentchain.customer.commom.CustomerRequestMapper;
import com.paymentchain.customer.commom.CustomerResponseMapper;
import com.paymentchain.customer.dto.CustomerRequest;
import com.paymentchain.customer.dto.CustomerResponse;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.exception.BusinessRuleException;
import com.paymentchain.customer.repository.CustomerRepository;
import com.paymentchain.customer.transactions.BusinessTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
@RequestMapping("/customer/V1")
@Tag(name = "Customer Controller", description = "API para usar customer")
public class CustomerRestController {
    
    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    BusinessTransaction businessTransaction;
    
    @Autowired
    CustomerRequestMapper request;
    
    @Autowired
    CustomerResponseMapper response;
    
    @Autowired
    private Environment env;
    
    @GetMapping("/check")
    public String getEnv(){
        return "Hello, your property value is: "+ env.getProperty("custom.activeprofileName");
    }
            
    
    @GetMapping()
    @Operation(description = "Retorna todos los customers de la base de datos")
    @ApiResponse(responseCode = "200", description = "exito")
    @ApiResponse(responseCode = "404", description = "no hay nada en la base de datos")
    public ResponseEntity<?> findAll() {
        List<Customer> findAll = customerRepository.findAll();
        List<CustomerResponse> customers = response.customerListToCustomerResponseList(findAll);
        if(findAll.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(customers);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable("id") long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable("id") long id, @RequestBody CustomerRequest input) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            Customer newCustomer = customer.get();
            newCustomer.setName(input.getName());
            newCustomer.setPhone(input.getPhone());
            newCustomer.setIban(input.getIban());
            newCustomer.setCode(input.getCode());
            newCustomer.setSurname(input.getSurname());
            Customer save = customerRepository.save(newCustomer);
            return new ResponseEntity<>(save, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody CustomerRequest input) throws BusinessRuleException, UnknownHostException {
        Customer customer = request.customerRequestToCustomer(input);
        Customer post = businessTransaction.post(customer);
        CustomerResponse cust = response.customerToCustomerResponse(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(cust);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        customerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/full")
    public Customer getByCode(@RequestParam("code") String code){
        Customer customer = businessTransaction.getByCode(code);
        return customer;
    }
    
    
    
}
