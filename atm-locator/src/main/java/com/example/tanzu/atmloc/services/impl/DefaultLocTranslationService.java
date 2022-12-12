package com.example.tanzu.atmloc.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.tanzu.atmloc.exchange.LocTranslationClient;
import com.example.tanzu.atmloc.model.Location;
import com.example.tanzu.atmloc.services.LocTranslationService;

import reactor.core.publisher.Mono;

@Service
@CacheConfig(cacheNames="geolocation")
public class DefaultLocTranslationService implements LocTranslationService
{
	protected LocTranslationClient locTransClient;
	
	@Autowired
	public void setLocTranslationClient(LocTranslationClient locTransClient)
	{
		this.locTransClient = locTransClient;
	}
	
	@Override
	@Cacheable
	public Mono<Location> translateLoc(String address, String city, String state) 
	{
		return locTransClient.translateLoc(address, city, state, "");
	}

	@Override
	@Cacheable
	public Mono<Location> translateLoc(String address, String postalCode) 
	{
		return locTransClient.translateLoc(address, "", "", postalCode);
	}

}
