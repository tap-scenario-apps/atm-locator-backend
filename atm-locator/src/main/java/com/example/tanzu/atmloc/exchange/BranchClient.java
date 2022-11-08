package com.example.tanzu.atmloc.exchange;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import reactor.core.publisher.Mono;

public interface BranchClient 
{
	@GetExchange("/{id}")
	public Mono<BranchSearchResult> findById(@PathVariable("id") Long id);
}
