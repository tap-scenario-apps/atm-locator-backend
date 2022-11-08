package com.example.tanzu.atm.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.tanzu.atm.entity.ATM;

public interface ATMRepository extends ReactiveCrudRepository<ATM, Long>, LocSearchATMRepository
{

}
