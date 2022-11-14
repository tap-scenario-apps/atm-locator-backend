package com.example.tanzu.atm.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("atmnotes")
public record ATMNote(@Id Long id, String note, @Column("atmId") Long atmId) 
{
}
