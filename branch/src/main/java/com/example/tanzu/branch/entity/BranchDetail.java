package com.example.tanzu.branch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("branchdetails")
public record BranchDetail(@Id Long id, String detail, @Column("branchId") Long branchId)  
{

}
