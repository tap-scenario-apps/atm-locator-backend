package com.example.tanzu.branch.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.tanzu.branch.entity.Branch;

public interface BranchRepository extends ReactiveCrudRepository<Branch, Long>
{

}
