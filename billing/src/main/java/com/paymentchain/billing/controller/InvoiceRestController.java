/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.billing.controller;

import com.paymentchain.billing.entities.Invoice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.paymentchain.billing.respository.InvoiceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.http.HttpStatus;

/**
 *
 * @author pablo
 */
@Tag(name="Billing API", description = "API para manejar facturas")
@RestController
@RequestMapping("/billing")
public class InvoiceRestController {
    
    @Autowired
    InvoiceRepository billingRepository;
    
    @Operation(description = "Devuelve todas las las facturas", summary = "Return 204 si no hay datos")
    @ApiResponse(responseCode = "200", description = "exito")
    @ApiResponse(responseCode = "500", description = "internal server error")
    @GetMapping()
    public List<Invoice> list() {
        return billingRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?>  get(@PathVariable long id) {
          Optional<Invoice> invoice = billingRepository.findById(id);
        if (invoice.isPresent()) {
            return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Invoice input) {
        return null;
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Invoice input) {
        Invoice save = billingRepository.save(input);
        return ResponseEntity.ok(save);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
         Optional<Invoice> dto = billingRepository.findById(Long.valueOf(id));
        if (!dto.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        billingRepository.delete(dto.get());
        return ResponseEntity.ok().build();
    }
    
}
