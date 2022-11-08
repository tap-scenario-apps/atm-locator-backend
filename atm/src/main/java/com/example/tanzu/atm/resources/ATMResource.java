package com.example.tanzu.atm.resources;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.tanzu.atm.entity.ATM;
import com.example.tanzu.atm.repository.ATMRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("atm")
@Slf4j
public class ATMResource 
{	
	private static final String DEFAULT_RADIUS = "10";
	
	protected ATMRepository atmRepo;
	
	@Autowired
	public void setATMRepository(ATMRepository atmRepo)
	{
		this.atmRepo = atmRepo;
	}
	
	@GetMapping
	public Flux<ATM> getAllATMs( Principal oauth2User)
	{
		return atmRepo.findAll()
    	   .onErrorResume(e -> { 
    	    	log.error("Error getting ATM.", e);
    	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    	   });
	}
	
	@GetMapping("/locsearch")
	public Flux<ATM> search(@RequestParam(name="latitude") float latitude, 
			@RequestParam(name="longitude") float longitude, 
			@RequestParam(name="radius", required=false, defaultValue=DEFAULT_RADIUS) int radius)
	{
		if (!isValidCordinates(latitude, longitude))
		{
			log.error("Invalid search paramater.");
			return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));				
		}
	
		return atmRepo.findByLocationDistance(latitude, longitude, radius);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ATM> addAtm(@RequestBody ATM atm)
	{
		log.info("Adding ATM with name {}", atm.name());
		
		if (!isValidCordinates(atm.latitude(), atm.longitude()))
		{
			log.error("Invalid coordinates.");
			return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));				
		}
	
		return atmRepo.save(atm);
	}	
	
	@DeleteMapping("{id}") 
	public Mono<Void> deleteATM(@PathVariable("id") Long id)
	{
		log.info("Deleting ATM with id {}", id);
		
		return atmRepo.deleteById(id)
    	   .onErrorResume(e -> { 
    	    	log.error("Error deleting ATM.", e);
    	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    	   });
	}	
	
	protected boolean isValidCordinates(float latitude, float longitude)
	{
		return (longitude < 180.0 && longitude > -180.0 && latitude < 90.0 && latitude > -90);
	}	
}
