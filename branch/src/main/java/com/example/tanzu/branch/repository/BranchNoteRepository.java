package com.example.tanzu.branch.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.tanzu.branch.entity.BranchNote;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchNoteRepository extends ReactiveCrudRepository<BranchNote, Long>
{
	public Flux<BranchNote> findByBranchId(Long atmId);
	
	public Mono<Void> deleteByBranchId(Long atmId);
}
