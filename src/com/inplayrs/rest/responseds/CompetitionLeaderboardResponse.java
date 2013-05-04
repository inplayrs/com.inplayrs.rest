package com.inplayrs.rest.responseds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompetitionLeaderboardResponse {

	@JsonProperty
	int rank;
	
	@JsonProperty
	String name;
	
	@JsonProperty
	int games_played;
	
	@JsonProperty
	int winnings;
	
	
	/*
	 * Default constructor
	 */
	public CompetitionLeaderboardResponse() {}


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

	
	public int getGames_played() {
		return games_played;
	}


	public void setGames_played(int games_played) {
		this.games_played = games_played;
	}

	
	public int getWinnings() {
		return winnings;
	}


	public void setWinnings(int winnings) {
		this.winnings = winnings;
	}



	
	
}
