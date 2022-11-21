package com.example.tanzu.atm.repository.impl.mariadb;

import com.example.tanzu.atm.repository.impl.ATMMapper;

public class MariaDBATMMapper extends ATMMapper
{
	protected static final double METERS_TO_MILES = 0.000621371192;

	@Override
	protected Double convertDistance(Double dist) 
	{
		return dist * METERS_TO_MILES;
	}

}
