/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.transactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.exception.BusinessRuleException;
import com.paymentchain.customer.repository.CustomerRepository;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author pablo
 */
@Service
public class BusinessTransaction {
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    
    private CustomerRepository customerRepository;
    
    public BusinessTransaction(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }
    
    
    //redefinido en la clase main
    /*private final WebClient.Builder webClientBuilder;

    public CustomerRestController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }*/
    
    HttpClient client = HttpClient.create()
            //timeout es el tiempo de espera maximo para la conexion entre cliente y servidor
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //tiempo maximo para recibir respuesta
            .responseTimeout(Duration.ofSeconds(1))
            //read timeout pasa cuando no se leea una data en cierto tiempo y write timeout lo mismo pero para una operacion de escritura
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });
    
    
    public Customer getByCode(@RequestParam("code") String code){
        Customer customer = customerRepository.findByCode(code);
        List<CustomerProduct> products = customer.getProducts();
        //buscar nombre producto
        products.forEach(x -> {
            try {
                String productName = getProductName(x.getId());
                x.setProductName(productName);
            } catch (UnknownHostException ex) {
                Logger.getLogger(BusinessTransaction.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //buscar transacciones
        List<?> transactions = getTransactions(customer.getIban());
        customer.setTransactions(transactions);
        return customer;
    }
    
    
    public Customer post(Customer input) throws BusinessRuleException, UnknownHostException {
        if(input.getProducts() != null){
            for(Iterator<CustomerProduct> it = input.getProducts().iterator(); it.hasNext();){
                CustomerProduct dto = it.next();
                String productName = getProductName(dto.getProductId());
                if(productName.isBlank()){
                    BusinessRuleException businessRuleException = new BusinessRuleException("1025", HttpStatus.PRECONDITION_FAILED,"Error validacion, producto con id "+dto.getProductId()+" no existe");
                    throw businessRuleException;
                }else{
                    dto.setCustomer(input);
                }
            }
        }
        Customer save = customerRepository.save(input);
        return save;
    }
    
    
    private String getProductName(long id) throws UnknownHostException {
        String name = "";
        try {
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-PRODUCT/product")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://BUSINESSDOMAIN-PRODUCT/product"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            name = block.get("name").asText();
        } catch (WebClientResponseException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return "";
            }else{
                throw new UnknownHostException(e.getMessage());
            }
        }
        
        return name;
    }
    
    private List<?> getTransactions(String iban){
         WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-TRANSACTIONS/transaction")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        List<?> transactions = build.method(HttpMethod.GET).uri(uriBuilder -> uriBuilder.path("/customer/transactions").queryParam("iban", iban).build()).retrieve().bodyToFlux(Object.class).collectList().block();
    
        return transactions;
    }
    
}
