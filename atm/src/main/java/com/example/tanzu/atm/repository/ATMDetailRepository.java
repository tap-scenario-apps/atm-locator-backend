package com.example.tanzu.atm.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.tanzu.atm.entity.ATMDetail;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ATMDetailRepository extends ReactiveCrudRepository<ATMDetail, Long>
{
	public Flux<ATMDetail> findByAtmId(Long atmId);
	
	public Mono<Void> deleteByAtmId(Long atmId);
}
