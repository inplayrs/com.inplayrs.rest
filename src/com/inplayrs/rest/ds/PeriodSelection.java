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
@Table(name = "period_selection")
public class PeriodSelection {

	@Id
	@Column(name = "selection_id")
	@GeneratedValue
	private int selection_id;
	
	@Column(name = "period", insertable = false, updatable = false)
	private int period_id;
	
	@Column(name = "game_entry", insertable = false, updatable = false)
	private int game_entry_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "period", nullable = false)
	private Period period;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game_entry_id", nullable = false)
	private GameEntry gameEntry;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public PeriodSelection() {
		
	}

	public int getSelection_id() {
		return selection_id;
	}

	public void setSelection_id(int selection_id) {
		this.selection_id = selection_id;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public GameEntry getGameEntry() {
		return gameEntry;
	}

	public void setGameEntry(GameEntry gameEntry) {
		this.gameEntry = gameEntry;
	}

	public int getPeriod_id() {
		return period_id;
	}

	public void setPeriod_id(int period_id) {
		this.period_id = period_id;
	}

	public int getGame_entry_id() {
		return game_entry_id;
	}

	public void setGame_entry_id(int game_entry_id) {
		this.game_entry_id = game_entry_id;
	}
	
	
	
	
}
