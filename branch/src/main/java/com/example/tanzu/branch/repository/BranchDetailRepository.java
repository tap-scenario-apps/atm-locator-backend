package com.example.tanzu.branch.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.tanzu.branch.entity.BranchDetail;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchDetailRepository extends ReactiveCrudRepository<BranchDetail, Long>
{
	public Flux<BranchDetail> findByBranchId(Long atmId);
	
	public Mono<Void> deleteByBranchId(Long atmId);
}
