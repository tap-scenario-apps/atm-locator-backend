package com.example.tanzu.atmmgmt.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.tanzu.atmmgmt.exchange.ATMClient;
import com.example.tanzu.atmmgmt.exchange.LocTranslationClient;
import com.example.tanzu.atmmgmt.model.ATM;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("atmmgmt")
@Slf4j
public class AtmMgmtResource 
{
	protected ATMClient atmClient;
	
	protected LocTranslationClient locService;
	
	@Autowired
	public void setATMClient(ATMClient atmClient)
	{
		this.atmClient = atmClient;
	}
	
	@Autowired
	public void setLocTranslationService(LocTranslationClient locService)
	{
		this.locService = locService;
	}
	
	@PutMapping()
	public Mono<ATM> addATM(@RequestBody ATM atm)
	{
		log.info("Adding ATM with name {} ", atm.name());
		
		return locService.translateLoc(atm.addr(), atm.city(), atm.state(), atm.postalCode())
		.flatMap(loc -> 
		{
			  final var newAtm = new ATM(null, atm.name(), 
					  loc.latitude(), 
					  loc.longitude(),
					  atm.addr(), 
					  atm.city(), 
					  atm.state(), 
					  atm.postalCode(), 
					  atm.distance(), 
					  atm.inDoors(),
					  atm.branchId(), null, null);	
			  
			  return atmClient.addATM(newAtm);
		})
 	   .onErrorResume(e -> 
 	   { 
	    	log.error("Error adding ATM with name {}.", atm.name(), e);
	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
	   });
	}
	
	@DeleteMapping("{id}") 
	public Mono<Void> deleteATM(@PathVariable("id") Long id)
	{
		log.info("Delete ATM with id {} ", id);
		
		return atmClient.deleteATM(id)
 	    .onErrorResume(e -> 
 	    { 
	    	log.error("Error delete ATM with id {}.", id, e);
	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
	    });
		
	}	
}
