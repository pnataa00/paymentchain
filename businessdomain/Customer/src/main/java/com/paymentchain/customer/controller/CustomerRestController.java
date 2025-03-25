/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.customer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.repository.CustomerRepository;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.Collections;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author pablo
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {
    
    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
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
            
    
    @Autowired
    private Environment env;
    
    @GetMapping("/check")
    public String getEnv(){
        return "Hello, your property value is: "+ env.getProperty("custom.activeprofileName");
    }
            
    
    @GetMapping()
    public List<Customer> findAll() {
        return customerRepository.findAll();
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
    public ResponseEntity<?> put(@PathVariable("id") long id, @RequestBody Customer input) {
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
    public ResponseEntity<?> post(@RequestBody Customer input) {
        //guardar productos en base de datos
        input.getProducts().forEach(x -> x.setCustomer(input));
        customerRepository.save(input);
        return ResponseEntity.ok(input);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        customerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/full")
    public Customer getByCode(@RequestParam("code") String code){
        Customer customer = customerRepository.findByCode(code);
        List<CustomerProduct> products = customer.getProducts();
        //buscar nombre producto
        products.forEach(x -> {
            String productName = getProductName(x.getId());
            x.setProductName(productName);
        });
        
        //buscar transacciones
        List<?> transactions = getTransactions(customer.getIban());
        customer.setTransactions(transactions);
        return customer;
    }
    
    private String getProductName(long id){
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-PRODUCT/product")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://BUSINESSDOMAIN-PRODUCT/product"))
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        String name = block.get("name").asText();
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
