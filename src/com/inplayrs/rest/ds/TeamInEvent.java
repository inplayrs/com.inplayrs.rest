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
@Table(name = "team_in_event")
public class TeamInEvent {

	@Id
	@Column(name = "tie_id")
	@GeneratedValue
	private int tie_id;
	
	@Column(name = "home_away")
	private char home_away;
	
	@Column(name = "score")
	private int score;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event", nullable = false)
	@JsonIgnore
	private Event event;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_in_competition", nullable = false)
	private TeamInCompetition team_in_competition;
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public TeamInEvent() {
		
	}


	public int getTie_id() {
		return tie_id;
	}


	public void setTie_id(int tie_id) {
		this.tie_id = tie_id;
	}


	public char getHome_away() {
		return home_away;
	}


	public void setHome_away(char home_away) {
		this.home_away = home_away;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public Event getEvent() {
		return event;
	}


	public void setEvent(Event event) {
		this.event = event;
	}


	public TeamInCompetition getTeam_in_competition() {
		return team_in_competition;
	}


	public void setTeam_in_competition(TeamInCompetition team_in_competition) {
		this.team_in_competition = team_in_competition;
	}
	
	
	
	
	
}
