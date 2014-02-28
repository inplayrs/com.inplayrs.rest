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

@Entity
@Table(name = "fangroup_comp_leaderboard")
public class FangroupCompLeaderboard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "competition", nullable = false)
	private Competition competition;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fangroup_id", nullable = false)
	private FanGroup fangroup;
	
	// TODO - Deco when leaderboard queries are updated to use HQL instead of SQL
	@Column(name = "fangroup_name")
	private String fangroupName;
	
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	@Column(name = "rank")
	private Integer rank;
	
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	@Column(name = "games_played")
	private Integer gamesPlayed;
	
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	@Column(name = "winnings")
	private Integer winnings;
	
	
	public FangroupCompLeaderboard() {
		
	}


	public Competition getCompetition() {
		return competition;
	}


	public void setCompetition(Competition competition) {
		this.competition = competition;
	}


	public FanGroup getFangroup() {
		return fangroup;
	}


	public void setFangroup(FanGroup fangroup) {
		this.fangroup = fangroup;
	}


	public String getFangroupName() {
		return fangroupName;
	}


	public void setFangroupName(String fangroupName) {
		this.fangroupName = fangroupName;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((competition == null) ? 0 : competition.hashCode());
		result = prime * result
				+ ((fangroup == null) ? 0 : fangroup.hashCode());
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
		FangroupCompLeaderboard other = (FangroupCompLeaderboard) obj;
		if (competition == null) {
			if (other.competition != null)
				return false;
		} else if (!competition.equals(other.competition))
			return false;
		if (fangroup == null) {
			if (other.fangroup != null)
				return false;
		} else if (!fangroup.equals(other.fangroup))
			return false;
		return true;
	}
	
	
}
