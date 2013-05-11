package com.inplayrs.rest.responseds;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompetitionPointsResponse {
	
	@Column(name = "global_rank")
	@JsonProperty
	private Integer global_rank;
	
	@Column(name = "global_winnings")
	@JsonProperty
	private Integer global_winnings;
	
	@Column(name = "fangroup_name")
	@JsonProperty
	private String fangroup_name;
	
	@Column(name = "fangroup_rank")
	@JsonProperty
	private Integer fangroup_rank;
	
	@Column(name = "fangroup_winnings")
	@JsonProperty
	private Integer fangroup_winnings;
	
	@Column(name = "user_in_fangroup_rank")
	@JsonProperty
	private Integer user_in_fangroup_rank;
	
	/*
	 * Default constructor
	 */
	public CompetitionPointsResponse() {}

	public Integer getGlobal_rank() {
		return global_rank;
	}

	public void setGlobal_rank(Integer global_rank) {
		this.global_rank = global_rank;
	}

	public Integer getGlobal_winnings() {
		return global_winnings;
	}

	public void setGlobal_winnings(Integer global_winnings) {
		this.global_winnings = global_winnings;
	}

	public String getFangroup_name() {
		return fangroup_name;
	}

	public void setFangroup_name(String fangroup_name) {
		this.fangroup_name = fangroup_name;
	}

	public Integer getFangroup_rank() {
		return fangroup_rank;
	}

	public void setFangroup_rank(Integer fangroup_rank) {
		this.fangroup_rank = fangroup_rank;
	}

	public Integer getFangroup_winnings() {
		return fangroup_winnings;
	}

	public void setFangroup_winnings(Integer fangroup_winnings) {
		this.fangroup_winnings = fangroup_winnings;
	}

	public Integer getUser_in_fangroup_rank() {
		return user_in_fangroup_rank;
	}

	public void setUser_in_fangroup_rank(Integer user_in_fangroup_rank) {
		this.user_in_fangroup_rank = user_in_fangroup_rank;
	}
	

	
}
