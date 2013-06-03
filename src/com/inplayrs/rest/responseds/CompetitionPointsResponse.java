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
	
	@Column(name = "global_pool_size")
	@JsonProperty
	private Integer global_pool_size;
	
	@Column(name = "num_fangroups_entered")
	@JsonProperty
	private Integer num_fangroups_entered;
	
	@Column(name = "fangroup_pool_size")
	@JsonProperty
	private Integer fangroup_pool_size;
	
	@Column(name = "total_global_winnings")
	@JsonProperty
	private Integer total_global_winnings;
	
	@Column(name = "total_fangroup_winnings")
	@JsonProperty
	private Integer total_fangroup_winnings;
	
	@Column(name = "total_userinfangroup_winnings")
	@JsonProperty
	private Integer total_userinfangroup_winnings;
	
	
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

	public Integer getGlobal_pool_size() {
		return global_pool_size;
	}

	public void setGlobal_pool_size(Integer global_pool_size) {
		this.global_pool_size = global_pool_size;
	}

	public Integer getNum_fangroups_entered() {
		return num_fangroups_entered;
	}

	public void setNum_fangroups_entered(Integer num_fangroups_entered) {
		this.num_fangroups_entered = num_fangroups_entered;
	}

	public Integer getFangroup_pool_size() {
		return fangroup_pool_size;
	}

	public void setFangroup_pool_size(Integer fangroup_pool_size) {
		this.fangroup_pool_size = fangroup_pool_size;
	}

	public Integer getTotal_global_winnings() {
		return total_global_winnings;
	}

	public void setTotal_global_winnings(Integer total_global_winnings) {
		this.total_global_winnings = total_global_winnings;
	}

	public Integer getTotal_fangroup_winnings() {
		return total_fangroup_winnings;
	}

	public void setTotal_fangroup_winnings(Integer total_fangroup_winnings) {
		this.total_fangroup_winnings = total_fangroup_winnings;
	}

	public Integer getTotal_userinfangroup_winnings() {
		return total_userinfangroup_winnings;
	}

	public void setTotal_userinfangroup_winnings(
			Integer total_userinfangroup_winnings) {
		this.total_userinfangroup_winnings = total_userinfangroup_winnings;
	}
	
	
}
