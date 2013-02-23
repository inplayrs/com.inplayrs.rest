package com.inplayrs.rest.ds;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "game_entry")
public class GameEntry {

	@Id
	@Column(name = "game_entry_id")
	@GeneratedValue
	private int game_entry_id;
	
	@Column(name = "game_instance", insertable = false, updatable = false)
	private int game_instance_id;
	
	@Column(name = "event_entry")
	private int event_entry_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_instance", nullable = false)
	private GameInstance gameInstance;
		
	@Column(name = "points")
	private int points;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "game_entry_id")
	private Set <PeriodEntry> periodEntries;
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public GameEntry() {
		
	}


	public int getGame_entry_id() {
		return game_entry_id;
	}


	public void setGame_entry_id(int game_entry_id) {
		this.game_entry_id = game_entry_id;
	}


	public GameInstance getGameInstance() {
		return gameInstance;
	}


	public void setGameInstance(GameInstance gameInstance) {
		this.gameInstance = gameInstance;
	}


	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}


	public Set<PeriodEntry> getPeriodEntries() {
		return periodEntries;
	}


	public void setPeriodEntries(Set<PeriodEntry> periodEntries) {
		this.periodEntries = periodEntries;
	}


	public int getGame_instance_id() {
		return game_instance_id;
	}


	public void setGame_instance_id(int game_instance_id) {
		this.game_instance_id = game_instance_id;
	}


	public int getEvent_entry_id() {
		return event_entry_id;
	}


	public void setEvent_entry_id(int event_entry_id) {
		this.event_entry_id = event_entry_id;
	}
	
	
	
	
	
}
