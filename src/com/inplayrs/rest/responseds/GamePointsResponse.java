package com.inplayrs.rest.responseds;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inplayrs.rest.ds.PeriodSelection;

@Entity
public class GamePointsResponse {


	// From game table
	@Column(name = "global_pot_size")
	@JsonProperty
	private Integer global_pot_size;
	
	@Column(name = "fangroup_pot_size")
	@JsonProperty
	private Integer fangroup_pot_size;
	
	// from global_game_leaderboard table
	@Column(name = "points")
	@JsonProperty
	private Integer points;
	
	@Column(name = "global_rank")
	@JsonProperty
	private Integer global_rank;
	
	// from fangroup_game_leaderboard table
	@Column(name = "fangroup_name")
	@JsonProperty
	private String fangroup_name;
	
	@Column(name = "fangroup_rank")
	@JsonProperty
	private Integer fangroup_rank;
	
	// from h2h_pool
	@Column(name = "h2h_user")
	@JsonProperty
	private String h2h_user;
	
	@Column(name = "h2h_points")
	@JsonProperty
	private Integer h2h_points;
	
	@Column(name = "h2h_pot_size")
	@JsonProperty
	private Integer h2h_pot_size;
	
	// from user_in_fangroup_game_leaderboard
	@Column(name = "user_in_fangroup_rank")
	@JsonProperty
	private Integer user_in_fangroup_rank;
	
	// from game_entry
	@Column(name = "global_winnings")
	@JsonProperty
	private Integer global_winnings;
	
	// from game_entry
	@Column(name = "fangroup_winnings")
	@JsonProperty
	private Integer fangroup_winnings;
	
	// from game_entry
	@Column(name = "h2h_winnings")
	@JsonProperty
	private Integer h2h_winnings;
	
	// from game_entry
	@Column(name = "total_winnings")
	@JsonProperty
	private Integer total_winnings;
	
	@JsonProperty
	private List<PeriodSelection> periodSelections;
	
	
	public GamePointsResponse() {

	}


	public Integer getGlobal_pot_size() {
		return global_pot_size;
	}


	public void setGlobal_pot_size(Integer global_pot_size) {
		this.global_pot_size = global_pot_size;
	}


	public Integer getFangroup_pot_size() {
		return fangroup_pot_size;
	}


	public void setFangroup_pot_size(Integer fangroup_pot_size) {
		this.fangroup_pot_size = fangroup_pot_size;
	}


	public Integer getPoints() {
		return points;
	}


	public void setPoints(Integer points) {
		this.points = points;
	}


	public Integer getGlobal_rank() {
		return global_rank;
	}


	public void setGlobal_rank(Integer global_rank) {
		this.global_rank = global_rank;
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


	public String getH2h_user() {
		return h2h_user;
	}


	public void setH2h_user(String h2h_user) {
		this.h2h_user = h2h_user;
	}


	public Integer getH2h_pot_size() {
		return h2h_pot_size;
	}


	public void setH2h_pot_size(Integer h2h_pot_size) {
		this.h2h_pot_size = h2h_pot_size;
	}
	

	public Integer getGlobal_winnings() {
		return global_winnings;
	}


	public void setGlobal_winnings(Integer global_winnings) {
		this.global_winnings = global_winnings;
	}


	public Integer getFangroup_winnings() {
		return fangroup_winnings;
	}


	public void setFangroup_winnings(Integer fangroup_winnings) {
		this.fangroup_winnings = fangroup_winnings;
	}


	public Integer getH2h_winnings() {
		return h2h_winnings;
	}


	public void setH2h_winnings(Integer h2h_winnings) {
		this.h2h_winnings = h2h_winnings;
	}


	public Integer getTotal_winnings() {
		return total_winnings;
	}


	public void setTotal_winnings(Integer total_winnings) {
		this.total_winnings = total_winnings;
	}


	public Integer getUser_in_fangroup_rank() {
		return user_in_fangroup_rank;
	}


	public void setUser_in_fangroup_rank(Integer user_in_fangroup_rank) {
		this.user_in_fangroup_rank = user_in_fangroup_rank;
	}


	public Integer getH2h_points() {
		return h2h_points;
	}


	public void setH2h_points(Integer h2h_points) {
		this.h2h_points = h2h_points;
	}


	public List<PeriodSelection> getPeriodSelections() {
		return periodSelections;
	}


	public void setPeriodSelections(List<PeriodSelection> periodSelections) {
		this.periodSelections = periodSelections;
	}


	
}
