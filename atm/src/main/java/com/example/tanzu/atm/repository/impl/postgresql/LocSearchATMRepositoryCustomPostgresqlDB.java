package com.example.tanzu.atm.repository.impl.postgresql;

import org.springframework.r2dbc.core.DatabaseClient;

import com.example.tanzu.atm.repository.impl.mariadb.LocSearchATMRepositoryCustomMariaDB;

public class LocSearchATMRepositoryCustomPostgresqlDB extends LocSearchATMRepositoryCustomMariaDB
{
	public LocSearchATMRepositoryCustomPostgresqlDB(DatabaseClient client)
	{
		super(client);
	}	
}
