package com.inplayrs.rest.ds;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "user_stats")
public class UserStats implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = false)
	@JsonIgnore
	private User user;
	
	@Column(name = "user", insertable=false, updatable = false)
	private Integer user_id;
	
	@Column(name = "total_winnings")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Integer total_winnings;
	
	@Column(name = "total_rank")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Integer total_rank;
	
	@Column(name = "total_games_played")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Integer total_games_played;
	
	@Column(name = "total_pc_correct")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Double total_pc_correct;
	
	@Column(name = "total_user_rating")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private String total_user_rating;
	
	@Column(name = "global_winnings")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Integer global_winnings;
	
	@Column(name = "fangroup_winnings")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Integer fangroup_winnings;
	
	@Column(name = "h2h_winnings")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Integer h2h_winnings;
	
	@Column(name = "global_games_won")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Integer global_games_won;
	
	@Column(name = "fangroup_pools_won")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Integer fangroup_pools_won;
	
	@Column(name = "h2h_won")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Integer h2h_won;
	
	@Column(name = "h2h_pc_correct")
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	private Double h2h_pc_correct;
	
	
	public UserStats() {
		
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Integer getUser_id() {
		return user_id;
	}


	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
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
	
	
	
	
}
