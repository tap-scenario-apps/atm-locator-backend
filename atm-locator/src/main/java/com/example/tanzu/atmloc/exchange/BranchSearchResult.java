package com.example.tanzu.atmloc.exchange;

import java.util.List;

import com.example.tanzu.atmloc.model.BranchHours;

public record BranchSearchResult(String name, float latitude, float longitude, String addr, String city, 
		String state, String postalCode, float distance, List<BranchHours> hours) 
{

}
