package com.example.tanzu.atm.repository;

import com.example.tanzu.atm.entity.ATM;
import com.example.tanzu.atm.entity.ATMD;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LocSearchATMRepository 
{
	Flux<ATMD> findByLocationDistance(float latitude, float longitude, int radius);
	
	Mono<ATM> saveATM(ATM atm);
}
