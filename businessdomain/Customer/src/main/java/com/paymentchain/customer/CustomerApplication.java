package com.paymentchain.customer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import static org.springframework.boot.devtools.restart.Restarter.disable;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}
        
        @Bean
        @LoadBalanced
        public WebClient.Builder loadBalancedWebClienBuilder(){
            return  WebClient.builder();
        }
        
        @Bean
        public OpenAPI customOpenAPI(){
            return new OpenAPI()
                    .components(new Components())
                    .info(new Info()
                        .title("Customer API")
                        .version("1.0.0"));
        }
        
        
        

}
