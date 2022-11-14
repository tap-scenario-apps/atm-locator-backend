package com.example.tanzu.atm.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;

import com.example.tanzu.atm.BaseTestPlan;
import com.example.tanzu.atm.SpringBaseTest;
import com.example.tanzu.atm.model.ATMModel;

import reactor.core.publisher.Mono;

public class ATMResource_associateBranchToATMTest extends SpringBaseTest
{
	abstract class TestPlan extends BaseTestPlan 
	{
		protected ATMModel addedATM;
		
		protected abstract ATMModel getATMToAdd();
		
		protected abstract Long getBranchToAssociate();
		
		@Override
		protected void performInner() throws Exception
		{	
			
			addedATM = webClient.put()
				.uri("/atm")
				.body(Mono.just(getATMToAdd()), ATMModel.class)
				.retrieve()
				.bodyToMono(ATMModel.class)
				.block();
	
			var updatedATM = webClient.post()
				.uri("/atm/branchAssoc/{atmId}/{branchId}", addedATM.id(), getBranchToAssociate())
				.retrieve()
				.bodyToMono(ATMModel.class)
				.block();
			
			doAssertions(updatedATM);
		}
		
		@Override
		protected void tearDown() 
		{
			if (addedATM != null)
			{
				webClient.delete()
						.uri("/atm/" + addedATM.id())
						.retrieve()
						.bodyToMono(Void.class)
						.block();
			}
		}
		
		protected abstract void doAssertions(ATMModel updatedAtm) throws Exception;
	}
	
	@Test
	public void testAssociateBranchToATM_assertAssociatedAdded() throws Exception
	{
		new TestPlan()
		{			
			@Override
			protected ATMModel getATMToAdd()
			{
				final var atm = new ATMModel(null, "Test ATM", 7.0f, -94.0f, "Test Addr", "Test City", "Test State", 
						"Test Postal", 0.0d, true,  null, null, null);
	
				return atm;
			}
			
			@Override
			protected Long getBranchToAssociate()
			{
				return 88L;
			}
			
			@Override
			protected void doAssertions(ATMModel updatedAtm) throws Exception
			{
				final var atm = getATMToAdd();
				compareATMs(atm, updatedAtm);
				
				assertEquals(getBranchToAssociate(), updatedAtm.branchId());
				
				// make sure the geo location didn't get destroyed as part of the update
				final var atms = atmRepo.findByLocationDistance(updatedAtm.latitude() - 1 , updatedAtm.longitude(), 100)
						.collectList().block();
				
				assertNotNull(atms);
				assertEquals(1, atms.size());
			
			}
		}.perform();
	}
	
	protected void compareATMs(ATMModel atm, ATMModel updatedAtm)
	{
		assertNotNull(updatedAtm.id());
		assertEquals(atm.addr(), updatedAtm.addr());
		assertEquals(atm.city(), updatedAtm.city());
		assertEquals(atm.inDoors(), updatedAtm.inDoors());
		assertEquals(atm.latitude(), updatedAtm.latitude());
		assertEquals(atm.longitude(), updatedAtm.longitude());
		assertEquals(atm.name(), updatedAtm.name());
		assertEquals(atm.postalCode(), updatedAtm.postalCode());
		assertEquals(atm.state(), updatedAtm.state());
	}
}
