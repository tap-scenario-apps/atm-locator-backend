package com.example.tanzu.atmloc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AtmLocatorSearchApplication 
{

	public static void main(String[] args) 
	{
		SpringApplication.run(AtmLocatorSearchApplication.class, args);
	}

}
