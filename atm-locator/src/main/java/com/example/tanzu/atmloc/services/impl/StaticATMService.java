package com.example.tanzu.atmloc.services.impl;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.example.tanzu.atmloc.model.ATM;
import com.example.tanzu.atmloc.model.Location;
import com.example.tanzu.atmloc.services.ATMService;

import reactor.core.publisher.Flux;

@Profile("static-search")
@Service
@Primary
public class StaticATMService implements ATMService
{
	@Override
	public Flux<ATM> search(String address, String city, String state, String postalCode, int radius)
	{
		return search(34.1398f, 118.3506f, radius);
	}

	@Override
	public Flux<ATM> search(float longitude, float latitude, int radius) 
	{
		final var cooridenates = new Location(longitude, latitude);
		
		var atm = new ATM(null, "Test ATM", cooridenates, "1313 Equator Lane", "Mockingbird Heights", "CA", 
				"91608", 10.6f, List.of("Next to It"), List.of("Run, Don't Walk"), true, null);
		
		return Flux.fromIterable(List.of(atm));
	}
}
