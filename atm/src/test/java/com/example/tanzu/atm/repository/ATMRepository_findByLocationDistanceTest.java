package com.example.tanzu.atm.repository;

import org.junit.jupiter.api.Test;

import com.example.tanzu.atm.SpringBaseTest;

import reactor.test.StepVerifier;

public class ATMRepository_findByLocationDistanceTest extends SpringBaseTest
{
	@Test
	public void testFindATMByLocationDistance() throws Exception
	{
		atmRepo.findByLocationDistance(39.033304f, -94.579736f, 6)
			.as(StepVerifier::create)
			.expectNextCount(1)
			.verifyComplete();
	}
}
