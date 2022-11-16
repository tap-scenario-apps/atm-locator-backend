package com.example.tanzu.branch.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.example.tanzu.branch.BaseTestPlan;
import com.example.tanzu.branch.SpringBaseTest;
import com.example.tanzu.branch.entity.BranchDetail;
import com.example.tanzu.branch.entity.BranchNote;
import com.example.tanzu.branch.model.BranchModel;

import reactor.core.publisher.Mono;

public class BranchResource_addBranchTest extends SpringBaseTest
{
	
	abstract class TestPlan extends BaseTestPlan 
	{
		protected BranchModel addedBranch;
		
		protected abstract BranchModel getBranchToAdd();
		
		@Override
		protected void performInner() throws Exception
		{	
			
			addedBranch = webClient.put()
				.uri("/branch")
				.body(Mono.just(getBranchToAdd()), BranchModel.class)
				.retrieve()
				.bodyToMono(BranchModel.class)
				.block();
	
			doAssertions(addedBranch);
		}
		
		@Override
		protected void tearDown() 
		{
			if (addedBranch != null)
			{
				webClient.delete()
						.uri("/branch/" + addedBranch.id())
						.retrieve()
						.bodyToMono(Void.class)
						.block();
			}
		}
		
		
		protected abstract void doAssertions(BranchModel addedBranch) throws Exception;
	}
	
	@Test
	public void testAddBranch_addNewAddress_noDetailsOrNotes_assertAdded() throws Exception
	{
		new TestPlan()
		{			
			@Override
			protected BranchModel getBranchToAdd()
			{
				final var branch = new BranchModel(null, "Test ATM", "Test Addr", "Test City", "Test State", 
						"Test Postal", null, null);
	
				return branch;
			}
			
			
			@Override
			protected void doAssertions(BranchModel addedBranch) throws Exception
			{
				final var branch = getBranchToAdd();
				compareBranches(branch, addedBranch);
				
				assertEquals(Collections.emptyList(), addedBranch.details());
				assertEquals(Collections.emptyList(), addedBranch.notes());
				
			}
		}.perform();
	}	
	
	@Test
	public void testAddBranch_addNewAddress_noDetailsWithNotes_assertAdded() throws Exception
	{
		new TestPlan()
		{			
			@Override
			protected BranchModel getBranchToAdd()
			{
				final var notes = Collections.singletonList(new BranchNote(null, "Test Note", null));
				
				final var branch = new BranchModel(null, "Test ATM", "Test Addr", "Test City", "Test State", 
						"Test Postal", null, notes);
	
				return branch;
			}
			
			
			@Override
			protected void doAssertions(BranchModel addedBranch) throws Exception
			{
				final var branch = getBranchToAdd();
				compareBranches(branch, addedBranch);
				
				assertEquals(Collections.emptyList(), addedBranch.details());
				assertEquals(1, addedBranch.notes().size());
				var note = addedBranch.notes().get(0);
				assertEquals(addedBranch.id(), note.branchId());
				assertEquals(branch.notes().get(0).note(), note.note());
				assertNotNull(note.id());
								
			}
		}.perform();
	}		
	
	@Test
	public void testAddBranch_addNewAddress_withDetailsNoNotes_assertAdded() throws Exception
	{
		new TestPlan()
		{			
			@Override
			protected BranchModel getBranchToAdd()
			{
				final var details = Collections.singletonList(new BranchDetail(null, "Test Detail", null));
				
				final var branch = new BranchModel(null, "Test ATM", "Test Addr", "Test City", "Test State", 
						"Test Postal", details, null);
	
				return branch;
			}
			
			
			@Override
			protected void doAssertions(BranchModel addedBranch) throws Exception
			{
				final var branch = getBranchToAdd();
				compareBranches(branch, addedBranch);

				assertEquals(Collections.emptyList(), addedBranch.notes());
				assertEquals(1, addedBranch.details().size());
				var detail = addedBranch.details().get(0);
				assertEquals(addedBranch.id(), detail.branchId());
				assertEquals(branch.details().get(0).detail(), detail.detail());
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
			protected BranchModel getBranchToAdd()
			{
				final var notes = Collections.singletonList(new BranchNote(null, "Test Note", null));
				final var details = Collections.singletonList(new BranchDetail(null, "Test Detail", null));
				
				final var branch = new BranchModel(null, "Test ATM", "Test Addr", "Test City", "Test State", 
						"Test Postal", details, notes);
	
				return branch;
			}
			
			
			@Override
			protected void doAssertions(BranchModel addedBranch) throws Exception
			{
				final var branch = getBranchToAdd();
				compareBranches(branch, addedBranch);


				assertEquals(1, addedBranch.notes().size());
				var note = addedBranch.notes().get(0);
				assertEquals(addedBranch.id(), note.branchId());
				assertEquals(branch.notes().get(0).note(), note.note());
				assertNotNull(note.id());
				
				assertEquals(1, addedBranch.details().size());
				var detail = addedBranch.details().get(0);
				assertEquals(addedBranch.id(), detail.branchId());
				assertEquals(branch.details().get(0).detail(), detail.detail());
				assertNotNull(detail.id());
				
			}
		}.perform();
	}	
	
	protected void compareBranches(BranchModel branch, BranchModel addedBranch)
	{
		assertNotNull(addedBranch.id());
		assertEquals(branch.addr(), addedBranch.addr());
		assertEquals(branch.city(), addedBranch.city());
		assertEquals(branch.name(), addedBranch.name());
		assertEquals(branch.postalCode(), addedBranch.postalCode());
		assertEquals(branch.state(), addedBranch.state());
	}
}
