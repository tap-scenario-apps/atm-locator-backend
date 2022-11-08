package com.example.tanzu.atmloc.services;

import com.example.tanzu.atmloc.model.Location;

import reactor.core.publisher.Mono;

public interface LocTranslationService 
{
	public Mono<Location> translateLoc(String address, String city, String state);
	
	public Mono<Location> translateLoc(String address, String zipCode);
}
