package com.example.tanzu.branch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public record Branch(@Id Long id, String name, String addr, String city, String state, 
		@Column("postalCode") String postalCode) 
{}
