package com.example.tanzu.atmloc.model;

import java.util.List;

public record ATM(Long id, String name, Location coordinates, String addr, String city, String state, String postalCode, float distance,
		List<String> details, List<String> notes, boolean inDoors, Branch branch) 
{

}
