/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.paymentchain.customer.commom;

import com.paymentchain.customer.dto.CustomerResponse;
import com.paymentchain.customer.entities.Customer;
import java.util.List;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 *
 * @author pablo
 */
@Mapper(componentModel = "spring")
public interface CustomerResponseMapper {
    
    Customer customerResponseToCustomer(CustomerResponse source);
    
    List<Customer> customerResponseListToCustomerList(List<CustomerResponse> source);
    
    @InheritInverseConfiguration
    CustomerResponse customerToCustomerResponse(Customer source);
    
    @InheritInverseConfiguration
    List<CustomerResponse> customerListToCustomerResponseList(List<Customer> source);
    
}
