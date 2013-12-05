package com.inplayrs.rest.responseds;


import com.fasterxml.jackson.annotation.JsonProperty;


public class UserStatsResponse {
	
	@JsonProperty
	private String username;
	
	@JsonProperty
	private Integer total_winnings;
	
	@JsonProperty
	private Integer total_rank;
	
	@JsonProperty
	private Integer total_games_played;
	
	@JsonProperty
	private Double total_pc_correct;
	
	@JsonProperty
	private String total_user_rating;
	
	@JsonProperty
	private Integer global_winnings;
	
	@JsonProperty
	private Integer fangroup_winnings;
	
	@JsonProperty
	private Integer h2h_winnings;
	
	@JsonProperty
	private Integer global_games_won;
	
	@JsonProperty
	private Integer fangroup_pools_won;
	
	@JsonProperty
	private Integer h2h_won;
	
	@JsonProperty
	private Double h2h_pc_correct;
	
	@JsonProperty
	private Integer num_users_in_system;
	
	public UserStatsResponse() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getTotal_winnings() {
		return total_winnings;
	}

	public void setTotal_winnings(Integer total_winnings) {
		this.total_winnings = total_winnings;
	}

	public Integer getTotal_rank() {
		return total_rank;
	}

	public void setTotal_rank(Integer total_rank) {
		this.total_rank = total_rank;
	}

	public Integer getTotal_games_played() {
		return total_games_played;
	}

	public void setTotal_games_played(Integer total_games_played) {
		this.total_games_played = total_games_played;
	}

	public Double getTotal_pc_correct() {
		return total_pc_correct;
	}

	public void setTotal_pc_correct(Double total_pc_correct) {
		this.total_pc_correct = total_pc_correct;
	}

	public String getTotal_user_rating() {
		return total_user_rating;
	}

	public void setTotal_user_rating(String total_user_rating) {
		this.total_user_rating = total_user_rating;
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

	public Integer getGlobal_games_won() {
		return global_games_won;
	}

	public void setGlobal_games_won(Integer global_games_won) {
		this.global_games_won = global_games_won;
	}

	public Integer getFangroup_pools_won() {
		return fangroup_pools_won;
	}

	public void setFangroup_pools_won(Integer fangroup_pools_won) {
		this.fangroup_pools_won = fangroup_pools_won;
	}

	public Integer getH2h_won() {
		return h2h_won;
	}

	public void setH2h_won(Integer h2h_won) {
		this.h2h_won = h2h_won;
	}

	public Double getH2h_pc_correct() {
		return h2h_pc_correct;
	}

	public void setH2h_pc_correct(Double h2h_pc_correct) {
		this.h2h_pc_correct = h2h_pc_correct;
	}

	public Integer getNum_users_in_system() {
		return num_users_in_system;
	}

	public void setNum_users_in_system(Integer num_users_in_system) {
		this.num_users_in_system = num_users_in_system;
	}


	
	
	
}
