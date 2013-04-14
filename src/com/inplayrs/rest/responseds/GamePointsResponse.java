package com.inplayrs.rest.responseds;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class GamePointsResponse {


	// From game table
	@Column(name = "global_pot_size")
	@JsonProperty
	private int global_pot_size;
	
	@Column(name = "fangroup_pot_size")
	@JsonProperty
	private int fangroup_pot_size;
	
	// from global_game_leaderboard table
	@Column(name = "points")
	@JsonProperty
	private int points;
	
	@Column(name = "global_rank")
	@JsonProperty
	private int global_rank;
	
	// from fangroup_game_leaderboard table
	@Column(name = "fangroup_name")
	@JsonProperty
	private String fangroup_name;
	
	@Column(name = "fangroup_rank")
	@JsonProperty
	private int fangroup_rank;
	
	// from h2h_pool
	@Column(name = "h2h_user")
	@JsonProperty
	private String h2h_user;
	
	@Column(name = "h2h_pot_size")
	@JsonProperty
	private int h2h_pot_size;
	
	// from user_in_fangroup_game_leaderboard
	@Column(name = "user_in_fangroup_rank")
	@JsonProperty
	private int user_in_fangroup_rank;
	
	// from game_entry
	@Column(name = "total_winnings")
	@JsonProperty
	private int total_winnings;
	
	
	public GamePointsResponse() {
		/*
		 * 

select 
	ge.user,
	ge.game as game_id,
	g.global_pot_size,
	g.fangroup_pot_size,
	h2h.h2h_pot_size,
	ge.total_winnings,
	(CASE 
    WHEN h2h.game_entry_1 = ge.game_entry_id then h2h.user_2
    WHEN h2h.game_entry_2 = ge.game_entry_id then h2h.user_1
    ELSE null
	END) as h2h_user,
	ggl.fangroup_name as fangroup_name,
	ggl.points,
	ggl.rank as global_rank,
	fgl.rank as fangroup_rank,
	uifgl.rank as user_in_fangroup_rank
from 
	game_entry ge
	left join game g on g.game_id = ge.game
	left join h2h_pool h2h on ( (h2h.game_entry_1 = ge.game_entry_id) or
										 (h2h.game_entry_2 = ge.game_entry_id) )
	left join global_game_leaderboard ggl on (ggl.game = ge.game and ggl.user = ge.user)
	left join fangroup_game_leaderboard fgl on (fgl.game = ge.game and fgl.fangroup_id = ggl.fangroup_id)
	left join user_in_fangroup_game_leaderboard uifgl on (uifgl.game = ge.game and uifgl.user = ge.user)
	
										 
										 
		 */
	}


	public int getGlobal_pot_size() {
		return global_pot_size;
	}


	public void setGlobal_pot_size(int global_pot_size) {
		this.global_pot_size = global_pot_size;
	}


	public int getFangroup_pot_size() {
		return fangroup_pot_size;
	}


	public void setFangroup_pot_size(int fangroup_pot_size) {
		this.fangroup_pot_size = fangroup_pot_size;
	}


	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}


	public int getGlobal_rank() {
		return global_rank;
	}


	public void setGlobal_rank(int global_rank) {
		this.global_rank = global_rank;
	}


	public String getFangroup_name() {
		return fangroup_name;
	}


	public void setFangroup_name(String fangroup_name) {
		this.fangroup_name = fangroup_name;
	}


	public int getFangroup_rank() {
		return fangroup_rank;
	}


	public void setFangroup_rank(int fangroup_rank) {
		this.fangroup_rank = fangroup_rank;
	}


	public String getH2h_user() {
		return h2h_user;
	}


	public void setH2h_user(String h2h_user) {
		this.h2h_user = h2h_user;
	}


	public int getH2h_pot_size() {
		return h2h_pot_size;
	}


	public void setH2h_pot_size(int h2h_pot_size) {
		this.h2h_pot_size = h2h_pot_size;
	}



	public int getTotal_winnings() {
		return total_winnings;
	}


	public void setTotal_winnings(int total_winnings) {
		this.total_winnings = total_winnings;
	}


	public int getUser_in_fangroup_rank() {
		return user_in_fangroup_rank;
	}


	public void setUser_in_fangroup_rank(int user_in_fangroup_rank) {
		this.user_in_fangroup_rank = user_in_fangroup_rank;
	}

	
	
}
