package com.example.tanzu.atmmgmt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.example.tanzu.atmmgmt.exchange.ATMClient;
import com.example.tanzu.atmmgmt.exchange.LocTranslationClient;


@Configuration
public class DeclarativeClientConfig 
{
	@Value("${services.location-transation.identifier}")
	protected String locServiceIdentifier;

	@Value("${services.atm.identifier}")
	protected String atmServiceIdentifier;
	
	@Bean
	public LocTranslationClient getLocTranslationClient()
	{
		final var client = WebClient.builder().baseUrl(locServiceIdentifier).build();
		final var factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();

		return factory.createClient(LocTranslationClient.class);
	}
	
	@Bean
	public ATMClient getATMClient()
	{
		final var client = WebClient.builder().baseUrl(atmServiceIdentifier).build();
		final var factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();

		return factory.createClient(ATMClient.class);
	}
}
