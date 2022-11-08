package com.example.tanzu.atm.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import com.example.tanzu.atm.repository.impl.LocSearchATMRepositoryCustom;
import com.example.tanzu.atm.repository.impl.h2.LocSearchATMRepositoryCustomH2;

@Configuration
public class CustomDBImplConfiguration 
{
	@ConditionalOnMissingBean
	@Bean
	public LocSearchATMRepositoryCustom h2CustomRepoImpl(DatabaseClient client)
	{
		return new LocSearchATMRepositoryCustomH2(client);
	}
}
