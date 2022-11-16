package com.example.tanzu.branch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("branchnotes")
public record BranchNote(@Id Long id, String note, @Column("branchId") Long branchId) 
{
}
