package com.inplayrs.rest.ds;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pool_comp_leaderboard")
public class PoolCompLeaderboard implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pool", nullable = false)
	private Competition competition;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pool", nullable = false)
	private Pool pool;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = false)
	private User user;
	
	@Column(name = "rank")
	private Integer rank;
	
	@Column(name = "games_played")
	private Integer gamesPlayed;
	
	@Column(name = "winnings")
	private Integer winnings;
	
	
	public PoolCompLeaderboard() {
		
	}


	public Competition getCompetition() {
		return competition;
	}


	public void setCompetition(Competition competition) {
		this.competition = competition;
	}


	public Pool getPool() {
		return pool;
	}


	public void setPool(Pool pool) {
		this.pool = pool;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Integer getRank() {
		return rank;
	}


	public void setRank(Integer rank) {
		this.rank = rank;
	}


	public Integer getGamesPlayed() {
		return gamesPlayed;
	}


	public void setGamesPlayed(Integer gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}


	public Integer getWinnings() {
		return winnings;
	}


	public void setWinnings(Integer winnings) {
		this.winnings = winnings;
	}
	
	
}
