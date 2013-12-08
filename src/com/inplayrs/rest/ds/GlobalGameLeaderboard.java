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
@Table(name = "global_game_leaderboard")
public class GlobalGameLeaderboard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game", nullable = false)
	private Game game;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = false)
	private User user;
	
	@Column(name = "rank")
	private Integer rank;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fangroup_id", nullable = false)
	private FanGroup fangroup;
	
	@Column(name = "points")
	private Integer points;
	
	@Column(name = "potential_winnings")
	private Integer potential_winnings;
	
	public GlobalGameLeaderboard() {
		
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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

	public FanGroup getFangroup() {
		return fangroup;
	}

	public void setFangroup(FanGroup fangroup) {
		this.fangroup = fangroup;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getPotential_winnings() {
		return potential_winnings;
	}

	public void setPotential_winnings(Integer potential_winnings) {
		this.potential_winnings = potential_winnings;
	}
	
	
	
}
