package com.example.tanzu.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ComponentScan({"com.example.tanzu.atm"})
@EnableR2dbcRepositories(basePackages="com.example.tanzu.atm.repository")
public class TestApplication 
{
    public static void main(String[] args) 
    {
        SpringApplication.run(TestApplication.class, args);
    }  
    
    @Bean
    public WebClient webClient()
    {
    	return WebClient.builder().baseUrl("http://localhost:8082").build();
    }
}
