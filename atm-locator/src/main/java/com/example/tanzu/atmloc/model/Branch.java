package com.example.tanzu.atmloc.model;

import java.util.List;

public record Branch(String name, Location coordinates, String addr, String city, String state, String postalCode, float distance, List<BranchHours> hours) 
{

}
