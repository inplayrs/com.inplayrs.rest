package com.inplayrs.rest.responseds;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class MyPoolResponse {

	@Column(name = "pool_id")
	@JsonProperty
	private Integer pool_id;
	
	@Column(name = "name")
	@JsonProperty
	private String name;
	
	public MyPoolResponse() {
		
	}

	public Integer getPool_id() {
		return pool_id;
	}

	public void setPool_id(Integer pool_id) {
		this.pool_id = pool_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
