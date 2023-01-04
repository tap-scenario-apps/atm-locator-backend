package com.example.tanzu.loctrans.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.tanzu.loctrans.model.Location;
import com.example.tanzu.loctrans.resources.service.GeoLocator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@OpenAPIDefinition(
        info = @Info(
                title = "Location Translator Service",
                version = "0.0.1",
        		description = "Service for translation a search location to geolocation coordinates"),
        tags = @Tag(
                name = "Location Translator REST API",
                description = "Location Translator Search API"))
@RestController
@RequestMapping("loc")
@Slf4j
public class LocationTranslationResource 
{	
	protected GeoLocator geoLoc;
	
	@Autowired
	public void setGeoLocator(GeoLocator geoLoc)
	{
		this.geoLoc = geoLoc;
	}
	
	@GetMapping()
	public Mono<Location> tranlateLocation(@RequestParam(name="address", required=false) String address,
			@RequestParam(name="city", required=false) String city, 
			@RequestParam(name="state", required=false) String state, @RequestParam(name="postalCode", required=false) String postalCode)
	{
		
		log.info("Searching for geo coordinates");
		
		if (!isValidQueryParams(city, state, postalCode))
		{
			log.error("Missing required search paramater.");
			return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));				
		}


		return geoLoc.translateToLocation(address, city, state, postalCode);
	}
	
	protected boolean isValidQueryParams(String city, String state, String postalCode)
	{
		return (isValidCityState(city, state) || isValidPostalCode(postalCode));
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
		
}
