package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "fan")
public class Fan {

	@Id
	@Column(name = "fan_id")
	@GeneratedValue
	private int fan_id;

	@Column(name = "pat_count")
	private int pat_count;
	
	@Column(name = "winnings")
	private double winnings;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_in_competition", nullable = false)
	private TeamInCompetition team_in_competition;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = false)
	private User user;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public Fan() {
		
	}

	public int getFan_id() {
		return fan_id;
	}

	public void setFan_id(int fan_id) {
		this.fan_id = fan_id;
	}

	public int getPat_count() {
		return pat_count;
	}

	public void setPat_count(int pat_count) {
		this.pat_count = pat_count;
	}

	public double getWinnings() {
		return winnings;
	}

	public void setWinnings(double winnings) {
		this.winnings = winnings;
	}

	public TeamInCompetition getTeam_in_competition() {
		return team_in_competition;
	}

	public void setTeam_in_competition(TeamInCompetition team_in_competition) {
		this.team_in_competition = team_in_competition;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
	
	
	
}
