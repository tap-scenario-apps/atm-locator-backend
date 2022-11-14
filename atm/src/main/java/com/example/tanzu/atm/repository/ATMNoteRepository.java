package com.example.tanzu.atm.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.tanzu.atm.entity.ATMNote;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ATMNoteRepository extends ReactiveCrudRepository<ATMNote, Long>
{
	public Flux<ATMNote> findByAtmId(Long atmId);
	
	public Mono<Void> deleteByAtmId(Long atmId);
}
