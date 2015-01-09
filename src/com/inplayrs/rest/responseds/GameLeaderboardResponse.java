package com.inplayrs.rest.responseds;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class GameLeaderboardResponse {

	@JsonProperty
	int rank;
	
	@JsonProperty
	String name;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL) 
	String fbID;
	
	@JsonProperty
	int points;
	
	@JsonProperty
	int potential_winnings;
	
	
	/*
	 * Default constructor
	 */
	public GameLeaderboardResponse() {}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getFbID() {
		return fbID;
	}


	public void setFbID(String fbID) {
		this.fbID = fbID;
	}


	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}


	public int getPotential_winnings() {
		return potential_winnings;
	}


	public void setPotential_winnings(int potential_winnings) {
		this.potential_winnings = potential_winnings;
	}


	
}
