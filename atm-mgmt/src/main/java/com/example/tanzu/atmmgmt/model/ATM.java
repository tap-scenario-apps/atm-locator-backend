package com.example.tanzu.atmmgmt.model;

import java.util.List;

public record ATM(Long id, String name, float latitude, float longitude, String addr, String city, String state, String postalCode, float distance,
     boolean inDoors, Long branchId, List<String> details, List<String> notes)
{

}
