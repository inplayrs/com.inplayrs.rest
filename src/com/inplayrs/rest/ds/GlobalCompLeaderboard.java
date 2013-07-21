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
@Table(name = "global_comp_leaderboard")
public class GlobalCompLeaderboard  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "competition", nullable = false)
	private Competition competition;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = false)
	private User user;
	
	@Column(name = "rank")
	private Integer rank;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fangroup_id", nullable = false)
	private FanGroup fangroup;
	
	@Column(name = "games_played")
	private Integer gamesPlayed;
	
	@Column(name = "winnings")
	private Integer winnings;
	
	
	public GlobalCompLeaderboard() {
		
	}


	public Competition getCompetition() {
		return competition;
	}


	public void setCompetition(Competition competition) {
		this.competition = competition;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((competition == null) ? 0 : competition.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GlobalCompLeaderboard other = (GlobalCompLeaderboard) obj;
		if (competition == null) {
			if (other.competition != null)
				return false;
		} else if (!competition.equals(other.competition))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}


	
	
}
