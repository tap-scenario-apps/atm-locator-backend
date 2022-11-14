package com.example.tanzu.atm.model;

import java.util.List;

import com.example.tanzu.atm.entity.ATMDetail;
import com.example.tanzu.atm.entity.ATMNote;

public record ATMModel(Long id, String name, Float latitude, Float longitude, String addr, String city, String state, 
 String postalCode, Double distance, boolean inDoors,  Long branchId, List<ATMDetail> details,  List<ATMNote> notes)
{

}
