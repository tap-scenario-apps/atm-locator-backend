package com.example.tanzu.atm.repository.impl;

import java.util.function.BiFunction;

import com.example.tanzu.atm.entity.ATMD;

import io.r2dbc.spi.Row;

public abstract class ATMMapper implements BiFunction<Row, Object, ATMD>
{

	@Override
	public ATMD apply(Row row, Object u) 
	{
		  return new ATMD(row.get("id", Long.class), (String)row.get("name", String.class), row.get("latitude", Float.class), 
				  row.get("longitude", Float.class), row.get("addr", String.class), row.get("city", String.class), row.get("state", String.class), 
				  row.get("postalCode", String.class), convertDistance(row.get("dist", Double.class)), row.get("inDoors", Boolean.class),
				  row.get("branchId", Long.class));
	}
	
	protected abstract Double convertDistance(Double dist);

}
