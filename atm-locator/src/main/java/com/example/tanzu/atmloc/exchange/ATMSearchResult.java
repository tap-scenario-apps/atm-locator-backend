package com.example.tanzu.atmloc.exchange;

import java.util.List;

public record ATMSearchResult(Long id, String name, float latitude, float longitude, String addr, String city, String state, String postalCode, float distance,
		List<String> details, List<String> notes, boolean inDoors, Long branchId) 
{

}
