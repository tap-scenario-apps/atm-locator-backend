package com.example.tanzu.atm.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("atmdetails")
public record ATMDetail(@Id Long id, String detail, @Column("atmId") Long atmId)  
{

}
