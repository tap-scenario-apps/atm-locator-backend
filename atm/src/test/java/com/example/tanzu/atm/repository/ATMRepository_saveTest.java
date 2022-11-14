package com.example.tanzu.atm.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.tanzu.atm.SpringBaseTest;
import com.example.tanzu.atm.entity.ATM;

import reactor.test.StepVerifier;


public class ATMRepository_saveTest extends SpringBaseTest
{
	@Test
	public void testSave() throws Exception
	{
		final var atm = new ATM(null, "Test", 0f, 0f, "Test Addr", "Test City", "Test State", "Test Postal",
				true, null);
		
		final var saveAtm = atmRepo.saveATM(atm).block();

		assertEquals(atm.addr(), saveAtm.addr());
		assertEquals(atm.branchId(), saveAtm.branchId());
		assertEquals(atm.city(), saveAtm.city());
		assertEquals(atm.inDoors(), saveAtm.inDoors());
		assertEquals(atm.latitude(), saveAtm.latitude());
		assertEquals(atm.longitude(), saveAtm.longitude());
		assertEquals(atm.name(), saveAtm.name());
		assertEquals(atm.state(), saveAtm.state());
		assertEquals(atm.postalCode(), saveAtm.postalCode());
		
		atmRepo.findById(saveAtm.id())
		.as(StepVerifier::create)
		.expectNextCount(1)
		.verifyComplete();
		
		atmRepo.deleteById(saveAtm.id()).block();
		
		atmRepo.findById(saveAtm.id())
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
}
