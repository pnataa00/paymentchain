/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.paymentchain.customer.commom;

import com.paymentchain.customer.dto.CustomerRequest;
import com.paymentchain.customer.entities.Customer;
import java.util.List;
import lombok.Data;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 *
 * @author pablo
 */
@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {
    
    Customer customerRequestToCustomer(CustomerRequest source);
    
    List<Customer> customerRequestListToCustomerList(List<CustomerRequest> source);
    
    @InheritInverseConfiguration
    CustomerRequest customerToCustomerRequest(Customer source);
    
    @InheritInverseConfiguration
    List<CustomerRequest> customerListToCustomerRequestList(List<Customer> source);
    
    
}
