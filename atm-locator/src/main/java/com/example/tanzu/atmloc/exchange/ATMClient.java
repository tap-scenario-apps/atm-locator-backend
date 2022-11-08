package com.example.tanzu.atmloc.exchange;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import reactor.core.publisher.Flux;

public interface ATMClient 
{
	@GetExchange("/atm/locsearch")
	public Flux<ATMSearchResult> search(@RequestParam float latitude, @RequestParam float longitude,  
			@RequestParam int radius, @RequestParam boolean branchLocOnly);
}
