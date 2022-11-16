package com.example.tanzu.atmloc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.tanzu.atmloc.exchange.ATMClient;
import com.example.tanzu.atmloc.exchange.ATMSearchResult;
import com.example.tanzu.atmloc.exchange.BranchClient;
import com.example.tanzu.atmloc.model.ATM;
import com.example.tanzu.atmloc.model.Branch;
import com.example.tanzu.atmloc.model.Location;
import com.example.tanzu.atmloc.services.ATMService;
import com.example.tanzu.atmloc.services.LocTranslationService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DefaultATMService implements ATMService
{
	protected LocTranslationService transService;
	
	protected ATMClient atmClient;
	
	protected BranchClient branchClient;
	
	@Autowired
	public void setLocTranslationService(LocTranslationService transService) 
	{
		this.transService = transService;
	}

	@Autowired
	public void setATMClient(ATMClient atmClient) 
	{
		this.atmClient = atmClient;
	}
	
	@Autowired
	public void setBranchClient(BranchClient branchClient) 
	{
		this.branchClient = branchClient;
	}
	
	@Override
	public Flux<ATM> search(String address, String city, String state, String postalCode, int radius)
	{
		final Mono<Location> locMono = StringUtils.hasText(postalCode) ? transService.translateLoc(address, postalCode) :
			transService.translateLoc(address, city, state);
				
		return Flux.from(locMono)
		.flatMap(loc -> 
		{
			return search(loc.latitude(), loc.longitude(), radius);
		});
	}
	
	@Override
	public Flux<ATM> search(float latitude, float longitude, int radius) 
	{
		return atmClient.search(latitude, longitude, radius, false)
			.flatMap(result -> 
			{
				if (result.branchId() == null)
					return Flux.fromIterable(List.of(resultToModel(result, null)));
				else
					return getBranch(result);
			});
	}
	
	protected Flux<ATM> getBranch(ATMSearchResult res)
	{
		return Flux.from(branchClient.findById(res.branchId())
			.map(branchRes -> 
			{
				/*
				 * TODO: If the ATM is indoors, check for vacation
				 * hours of the branch and add it as a note if they exist for the current week.
				 */
				
				return resultToModel(res, branchRes);
			}));
	}
	
	protected ATM resultToModel(ATMSearchResult atmRes, Branch branch)
	{		
		final var atmLoc = new Location(atmRes.latitude(), atmRes.longitude());
		
		return new ATM(atmRes.id(), atmRes.name(), atmLoc, atmRes.addr(), atmRes.city(), atmRes.state(), 
				atmRes.postalCode(), atmRes.distance(), atmRes.details(), atmRes.notes(), atmRes.inDoors(), branch);
	}
}
