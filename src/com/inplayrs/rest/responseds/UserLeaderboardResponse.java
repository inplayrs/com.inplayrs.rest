package com.inplayrs.rest.responseds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserLeaderboardResponse {

	@JsonProperty
	private Integer rank;
	
	@JsonProperty
	private String username;
	
	@JsonProperty
	private Integer winnings;
	
	
	public UserLeaderboardResponse() {
		
	}


	public Integer getRank() {
		return rank;
	}


	public void setRank(Integer rank) {
		this.rank = rank;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public Integer getWinnings() {
		return winnings;
	}


	public void setWinnings(Integer winnings) {
		this.winnings = winnings;
	}
	
	
	

	
	
}
