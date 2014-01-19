package com.inplayrs.rest.responseds;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class PoolMemberResponse {

	@Column(name = "user_id")
	@JsonProperty
	private Integer user_id;
	
	@Column(name = "username")
	@JsonProperty
	private String username;
	
	@Column(name = "facebook_id")
	@JsonProperty
	private String facebook_id;
	
	public PoolMemberResponse() {
		
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFacebook_id() {
		return facebook_id;
	}

	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}
	
	
	
}
