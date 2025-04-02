/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.setups;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 * @author pablo
 */
@Component
public class GlobalPreFilter implements GlobalFilter{
    private static final Logger logger = LoggerFactory.getLogger(GlobalPreFilter.class);
    /**
     *
     * @param exhange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exhange, GatewayFilterChain chain){
        logger.info("Global prefilter executed");
        return chain.filter(exhange);
    }
    
}
