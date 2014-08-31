package com.inplayrs.rest.responseds;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class UserTrophyResponse {

	@Column(name = "trophy_id")
	@JsonProperty
	private Integer trophy_id;
	
	@Column(name = "name")
	@JsonProperty
	private String name;
	
	@Column(name = "achieved")
	@JsonProperty
	private Boolean achieved;
	
	public UserTrophyResponse() {
		
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

	public Boolean getAchieved() {
		return achieved;
	}

	public void setAchieved(Boolean achieved) {
		this.achieved = achieved;
	}
	
	
	
	
}
