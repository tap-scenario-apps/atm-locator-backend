package com.example.tanzu.branch.model;

import java.util.List;

import com.example.tanzu.branch.entity.BranchDetail;
import com.example.tanzu.branch.entity.BranchNote;

public record BranchModel(Long id, String name, String addr, String city, String state, String postalCode,
		List<BranchDetail> details, List<BranchNote> notes) 
{

}
