package com.example.tanzu.atm.repository;

import com.example.tanzu.atm.entity.ATM;
import com.example.tanzu.atm.entity.ATMD;
import com.example.tanzu.atm.repository.impl.LocSearchATMRepositoryCustom;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Wrapper DB specific location searches and inserts.  Delegates to a specific implementation
 * of the LocSearchATMRepositoryCustom interface.
 * @author Greg Meyer
 *
 */
public class LocSearchATMRepositoryImpl implements LocSearchATMRepository
{	
	protected LocSearchATMRepositoryCustom customRepo;
	
	public LocSearchATMRepositoryImpl(LocSearchATMRepositoryCustom customRepo)
	{
		this.customRepo = customRepo;
	}

	@Override
	public Flux<ATMD> findByLocationDistance(float latitude, float longitude, int radius) 
	{
		return customRepo.findByLocationDistance(latitude, longitude, radius);
	}

	@Override
	public Mono<ATM> saveATM(ATM atm) 
	{
		return customRepo.saveATM(atm);
	}
	
}
