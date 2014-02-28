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
@Table(name = "user_in_fangroup_game_leaderboard")
public class UserInFangroupGameLeaderboard implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game", nullable = false)
	private Game game;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = false)
	private User user;
	
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
	@Column(name = "points")
	private Integer points;
	
	@Generated(GenerationTime.ALWAYS)   // calculated by admin scripts
	@Column(name = "potential_winnings")
	private Integer potential_winnings;
	
	
	public UserInFangroupGameLeaderboard() {
		
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
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
		UserInFangroupGameLeaderboard other = (UserInFangroupGameLeaderboard) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	
}
