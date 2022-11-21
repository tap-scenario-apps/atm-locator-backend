package com.example.tanzu.atm.repository.impl.h2;

import com.example.tanzu.atm.repository.impl.ATMMapper;

public class H2ATMMapper extends ATMMapper
{
	protected static final Double DISTANCE_TO_MILES = 69.0;

	@Override
	protected Double convertDistance(Double dist) 
	{
		return dist * DISTANCE_TO_MILES;
	}
}
