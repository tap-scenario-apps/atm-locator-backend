package com.example.tanzu.atmmgmt.exchange;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.example.tanzu.atmmgmt.model.Location;

import reactor.core.publisher.Mono;

public interface LocTranslationClient 
{
	@GetExchange("/loc")
	public Mono<Location> translateLoc(@RequestParam(required=false) String address, @RequestParam(required=false) String city, 
	@RequestParam(required=false) String state, @RequestParam(required=false) String postalCode);
}
