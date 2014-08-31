package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trophy")
public class Trophy {

	@Id
	@Column(name = "trophy_id")
	@GeneratedValue
	private Integer trophy_id;
	
	@Column(name = "name", unique=true)
	private String name;
	
	public Trophy() {
		
	}

	public Integer getTrophy_id() {
		return trophy_id;
	}

	public void setTrophy_id(Integer trophy_id) {
		this.trophy_id = trophy_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
