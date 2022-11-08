package com.example.tanzu.atmmgmt.exchange;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.example.tanzu.atmmgmt.model.ATM;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ATMClient 
{
	@GetExchange("/atm/locsearch")
	public Flux<ATM> search(@RequestParam float latitude, @RequestParam float longitude,  
			@RequestParam int radius, @RequestParam boolean branchLocOnly);
	
	@PutExchange("/atm")
	public Mono<ATM> addATM(@RequestBody ATM atm);
	
	@DeleteExchange("/{id}")
	public Mono<Void> deleteATM(@PathVariable("id") Long id);
}
