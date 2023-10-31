package com.example.tanzu.loctrans.resources.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.example.tanzu.loctrans.model.Location;
import com.example.tanzu.loctrans.resources.service.GeoLocator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GeocodIODemoLocator implements GeoLocator
{
	private static final String BASE_URL = "https://api.geocod.io/v1.7/geocode";
	
	@Override
	public Mono<Location> translateToLocation(String address, String city, String state, String postalCode) 
	{
		final var builder = StringUtils.hasText(address) ? new StringBuilder(address) : new StringBuilder("");
		
		final var query = (StringUtils.hasText(postalCode)) ? builder.append(" ").append(postalCode) 
				: builder.append(" ").append(city).append(", ").append(state).toString();
		
		return WebClient.builder().baseUrl(BASE_URL).build()
				  .get().uri(uriBuilder -> uriBuilder
						  .queryParam("q", query)
						  .queryParam("api_key", "DEMO").build())
				  .retrieve()
				  .bodyToMono(SearchRes.class)
				  .flatMap(searchRes -> 
				  {
					  if (searchRes.results() != null && searchRes.results().size() > 0)
					  {
						  var res = searchRes.results().get(0); 
						  return Mono.just(new Location(res.location().lat(), res.location().lng()));
					  }

					  return Mono.empty();
				  })
				  .onErrorResume(e -> 
				  {
					 if (e instanceof WebClientResponseException)
					 {
						 /*
						  * GeocodIO return 422 if it can't process the information we gave it.  This
						  * includes non-existent zip codes.
						  */
						 WebClientResponseException ex = (WebClientResponseException)e;
						 if (ex.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY)
						 {
							 log.error("Error translating ATM location due to an unprocessable entity.", e.getMessage());
							 return Mono.empty();
						 }
					 }
					 
					  
	    	    	 log.error("Error translating ATM location: {}  {}.", e.getMessage(), e.getCause());
	    	    	 return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));					  
				  });
	}

	record Loc(float lat, float lng) {};
	
	record Results(Loc location) {};
	
	record SearchRes(List<Results> results) {}
}
