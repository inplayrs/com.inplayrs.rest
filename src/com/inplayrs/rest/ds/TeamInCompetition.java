package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "team_in_competition")
public class TeamInCompetition {

	@Id
	@Column(name = "tic_id")
	@GeneratedValue
	private int tic_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team", nullable = false)
	private Team team;
	
	@Column(name = "pat_count")
	private int pat_count;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public TeamInCompetition() {
	}

	public int getTic_id() {
		return tic_id;
	}

	public void setTic_id(int tic_id) {
		this.tic_id = tic_id;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getPat_count() {
		return pat_count;
	}

	public void setPat_count(int pat_count) {
		this.pat_count = pat_count;
	}	
	
	
}
