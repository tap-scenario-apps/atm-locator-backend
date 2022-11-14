package com.example.tanzu.atm.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

public record ATMD(@Id Long id, String name, Float latitude, Float longitude, String addr, String city, String state, 
@Column("postalCode") String postalCode, @Transient Double distance, @Column("inDoors") boolean inDoors, @Column("branchId") Long branchId) 
{

}
