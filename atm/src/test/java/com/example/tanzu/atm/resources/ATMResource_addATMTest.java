package com.example.tanzu.atm.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.example.tanzu.atm.BaseTestPlan;
import com.example.tanzu.atm.SpringBaseTest;
import com.example.tanzu.atm.entity.ATMDetail;
import com.example.tanzu.atm.entity.ATMNote;
import com.example.tanzu.atm.model.ATMModel;

import reactor.core.publisher.Mono;

public class ATMResource_addATMTest extends SpringBaseTest
{
	
	abstract class TestPlan extends BaseTestPlan 
	{
		protected ATMModel addedATM;
		
		protected abstract ATMModel getATMToAdd();
		
		@Override
		protected void performInner() throws Exception
		{	
			
			addedATM = webClient.put()
				.uri("/atm")
				.body(Mono.just(getATMToAdd()), ATMModel.class)
				.retrieve()
				.bodyToMono(ATMModel.class)
				.block();
	
			doAssertions(addedATM);
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
		
		
		protected abstract void doAssertions(ATMModel addedAtm) throws Exception;
	}
	
	@Test
	public void testAddATM_addNewAddress_noDetailsOrNotes_assertAdded() throws Exception
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
			protected void doAssertions(ATMModel addedAtm) throws Exception
			{
				final var atm = getATMToAdd();
				compareATMs(atm, addedAtm);
				
				assertEquals(Collections.emptyList(), addedAtm.details());
				assertEquals(Collections.emptyList(), addedAtm.notes());
				
			}
		}.perform();
	}	
	
	@Test
	public void testAddATM_addNewAddress_noDetailsWithNotes_assertAdded() throws Exception
	{
		new TestPlan()
		{			
			@Override
			protected ATMModel getATMToAdd()
			{
				final var notes = Collections.singletonList(new ATMNote(null, "Test Note", null));
				
				final var atm = new ATMModel(null, "Test ATM", 7.0f, -94.0f, "Test Addr", "Test City", "Test State", 
						"Test Postal", 0.0d, true,  null, null, notes);
	
				return atm;
			}
			
			
			@Override
			protected void doAssertions(ATMModel addedAtm) throws Exception
			{
				final var atm = getATMToAdd();
				compareATMs(atm, addedAtm);
				
				assertEquals(Collections.emptyList(), addedAtm.details());
				assertEquals(1, addedAtm.notes().size());
				var note = addedAtm.notes().get(0);
				assertEquals(addedAtm.id(), note.atmId());
				assertEquals(atm.notes().get(0).note(), note.note());
				assertNotNull(note.id());
								
			}
		}.perform();
	}		
	
	@Test
	public void testAddATM_addNewAddress_withDetailsNoNotes_assertAdded() throws Exception
	{
		new TestPlan()
		{			
			@Override
			protected ATMModel getATMToAdd()
			{
				final var details = Collections.singletonList(new ATMDetail(null, "Test Detail", null));
				
				final var atm = new ATMModel(null, "Test ATM", 7.0f, -94.0f, "Test Addr", "Test City", "Test State", 
						"Test Postal", 0.0d, true,  null, details, null);
	
				return atm;
			}
			
			
			@Override
			protected void doAssertions(ATMModel addedAtm) throws Exception
			{
				final var atm = getATMToAdd();
				compareATMs(atm, addedAtm);

				assertEquals(Collections.emptyList(), addedAtm.notes());
				assertEquals(1, addedAtm.details().size());
				var detail = addedAtm.details().get(0);
				assertEquals(addedAtm.id(), detail.atmId());
				assertEquals(atm.details().get(0).detail(), detail.detail());
				assertNotNull(detail.id());
				
			}
		}.perform();
	}	
	
	@Test
	public void testAddATM_addNewAddress_withDetailsWithNotes_assertAdded() throws Exception
	{
		new TestPlan()
		{			
			@Override
			protected ATMModel getATMToAdd()
			{
				final var notes = Collections.singletonList(new ATMNote(null, "Test Note", null));
				final var details = Collections.singletonList(new ATMDetail(null, "Test Detail", null));
				
				final var atm = new ATMModel(null, "Test ATM", 7.0f, -94.0f, "Test Addr", "Test City", "Test State", 
						"Test Postal", 0.0d, true,  null, details, notes);
	
				return atm;
			}
			
			
			@Override
			protected void doAssertions(ATMModel addedAtm) throws Exception
			{
				final var atm = getATMToAdd();
				compareATMs(atm, addedAtm);


				assertEquals(1, addedAtm.notes().size());
				var note = addedAtm.notes().get(0);
				assertEquals(addedAtm.id(), note.atmId());
				assertEquals(atm.notes().get(0).note(), note.note());
				assertNotNull(note.id());
				
				assertEquals(1, addedAtm.details().size());
				var detail = addedAtm.details().get(0);
				assertEquals(addedAtm.id(), detail.atmId());
				assertEquals(atm.details().get(0).detail(), detail.detail());
				assertNotNull(detail.id());
				
			}
		}.perform();
	}	
	
	protected void compareATMs(ATMModel atm, ATMModel addedAtm)
	{
		assertNotNull(addedAtm.id());
		assertEquals(atm.addr(), addedAtm.addr());
		assertEquals(atm.branchId(), addedAtm.branchId());
		assertEquals(atm.city(), addedAtm.city());
		assertEquals(atm.distance(), addedAtm.distance());
		assertEquals(atm.inDoors(), addedAtm.inDoors());
		assertEquals(atm.latitude(), addedAtm.latitude());
		assertEquals(atm.longitude(), addedAtm.longitude());
		assertEquals(atm.name(), addedAtm.name());
		assertEquals(atm.postalCode(), addedAtm.postalCode());
		assertEquals(atm.state(), addedAtm.state());
	}
}
