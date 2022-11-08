package com.example.tanzu.atmloc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.tanzu.atmloc.model.ATM;
import com.example.tanzu.atmloc.services.ATMService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("atmsearch")
@Slf4j
public class ATMSearchResource 
{
	/*
	 * Coordinates are measured in degrees latitude and longitude and have a range of:
	 * Latitude:  -90.0 to 90.0
	 * Longitude: -180.0 to 180.0
	 * This constant indicates an empty 
	 */
	private static final String EMPTY_COORDINATE = "1000.0";

	private static final String DEFAULT_RADIUS = "10";
	
	protected ATMService atmSvc;
	
	@Autowired
	public void setATMSearch(ATMService atmSvc)
	{
		this.atmSvc = atmSvc;
	}
	
	@GetMapping()
	public Flux<ATM> search(@RequestParam(name="address", required=false) String address, 
			@RequestParam(name="city", required=false) String city, 
			@RequestParam(name="state", required=false) String state, @RequestParam(name="postalCode", required=false) String postalCode,
			@RequestParam(name="latitude", required=false, defaultValue=EMPTY_COORDINATE) Float latitude, 
			@RequestParam(name="longitude", required=false, defaultValue=EMPTY_COORDINATE) Float longitude, 
			@RequestParam(name="radius", required=false, defaultValue=DEFAULT_RADIUS) int radius)
	{
		if (!isValidQueryParams(city, state, postalCode, latitude, longitude))
		{
			log.error("Missing required search paramater.");
			return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));				
		}
		
		final Flux<ATM> processFlux = (isValidCordinates(latitude, longitude)) ?  atmSvc.search(latitude, longitude, radius) :
			atmSvc.search(address, city, state, postalCode, radius);
		
		return processFlux
    	   .onErrorResume(e -> 
    	   { 
    	    	log.error("Error search for ATM locations search.", e);
    	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    	   });
	}
	
	protected boolean isValidQueryParams(String city, String state, String postalCode, float latitude, float longitude)
	{
		return (isValidCityState(city, state) || isValidPostalCode(postalCode) || isValidCordinates(latitude, longitude));
	}
	
	protected boolean isValidCityState(String city, String state)
	{
		return  ((!StringUtils.hasText(city) && !StringUtils.hasText(state)) || 
				(StringUtils.hasText(city) && !StringUtils.hasText(state)) || 
			    (!StringUtils.hasText(city) && StringUtils.hasText(state))) ? false : true;

	}
	
	protected boolean isValidPostalCode(String postalCode)
	{
		return StringUtils.hasText(postalCode);
	}
	
	protected boolean isValidCordinates(float latitude, float longitude)
	{
		return (longitude < 180.0 && longitude > -180.0 && latitude < 90.0 && latitude > -90);
	}
}
