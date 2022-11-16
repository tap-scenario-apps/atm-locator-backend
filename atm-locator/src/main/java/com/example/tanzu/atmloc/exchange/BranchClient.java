package com.example.tanzu.atmloc.exchange;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import com.example.tanzu.atmloc.model.Branch;

import reactor.core.publisher.Mono;

public interface BranchClient 
{
	@GetExchange("/{id}")
	public Mono<Branch> findById(@PathVariable("id") Long id);
}
