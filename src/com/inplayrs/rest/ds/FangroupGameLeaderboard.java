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
@Table(name = "fangroup_game_leaderboard")
public class FangroupGameLeaderboard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game", nullable = false)
	private Game game;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fangroup_id", nullable = false)
	private FanGroup fangroup;
	
	// TODO - Deco when leaderboard queries are updated to use HQL instead of SQL
	@Column(name = "fangroup_name")
	private String fangroupName;
	
	@Generated(GenerationTime.ALWAYS)	// calculated by admin scripts
	@Column(name = "rank")
	private Integer rank;
	
	@Generated(GenerationTime.ALWAYS)   // calculated by admin scripts
	@Column(name = "total_points")
	private Integer totalPoints;
	
	@Generated(GenerationTime.ALWAYS)   // calculated by admin scripts
	@Column(name = "avg_points")
	private Integer avgPoints;
	
	@Generated(GenerationTime.ALWAYS)   // calculated by admin scripts
	@Column(name = "potential_winnings")
	private Integer potentialWinnings;
	
	@Generated(GenerationTime.ALWAYS)   // calculated by admin scripts
	@Column(name = "num_players")
	private Integer numPlayers;
	
	
	public FangroupGameLeaderboard() {
		
	}


	public Game getGame() {
		return game;
	}


	public void setGame(Game game) {
		this.game = game;
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


	public Integer getTotalPoints() {
		return totalPoints;
	}


	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}


	public Integer getAvgPoints() {
		return avgPoints;
	}


	public void setAvgPoints(Integer avgPoints) {
		this.avgPoints = avgPoints;
	}


	public Integer getPotentialWinnings() {
		return potentialWinnings;
	}


	public void setPotentialWinnings(Integer potentialWinnings) {
		this.potentialWinnings = potentialWinnings;
	}


	public Integer getNumPlayers() {
		return numPlayers;
	}


	public void setNumPlayers(Integer numPlayers) {
		this.numPlayers = numPlayers;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fangroup == null) ? 0 : fangroup.hashCode());
		result = prime * result + ((game == null) ? 0 : game.hashCode());
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
		FangroupGameLeaderboard other = (FangroupGameLeaderboard) obj;
		if (fangroup == null) {
			if (other.fangroup != null)
				return false;
		} else if (!fangroup.equals(other.fangroup))
			return false;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		return true;
	}
	
	
	
	
	
}
