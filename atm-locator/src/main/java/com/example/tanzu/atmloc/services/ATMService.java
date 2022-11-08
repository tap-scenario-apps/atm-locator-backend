package com.example.tanzu.atmloc.services;

import com.example.tanzu.atmloc.model.ATM;

import reactor.core.publisher.Flux;

public interface ATMService 
{
	public Flux<ATM> search(String address, String city, String state, String postalCode, int radius);
	
	public Flux<ATM> search(float longitude, float latitude, int radius);
}
