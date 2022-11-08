package com.example.tanzu.loctrans.resources.service;

import com.example.tanzu.loctrans.model.Location;

import reactor.core.publisher.Mono;

public interface GeoLocator 
{
	public Mono<Location> translateToLocation(String address, String city, String state, String postalCode);
}
