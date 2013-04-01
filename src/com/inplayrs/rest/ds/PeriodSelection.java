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
@Table(name = "period_selection")
public class PeriodSelection {

	@Id
	@Column(name = "period_selection_id")
	@GeneratedValue
	private int period_selection_id;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game_entry", nullable = false)
	private GameEntry gameEntry;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "period", nullable = false)
	private Period period;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "period_option", nullable = false)
	private PeriodOption periodOption;
	
	@Column(name = "potential_points")
	private int potential_points;
	
	@Column(name = "awarded_points")
	private int awarded_points;
	
	@Column(name = "cashed_out")
	private boolean cashed_out;
	
	@Column(name = "processed")
	@JsonIgnore
	private boolean processed;

	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public PeriodSelection() {
		
	}



	public int getPeriod_selection_id() {
		return period_selection_id;
	}



	public void setPeriod_selection_id(int period_selection_id) {
		this.period_selection_id = period_selection_id;
	}



	public GameEntry getGameEntry() {
		return gameEntry;
	}



	public void setGameEntry(GameEntry gameEntry) {
		this.gameEntry = gameEntry;
	}



	public Period getPeriod() {
		return period;
	}



	public void setPeriod(Period period) {
		this.period = period;
	}



	public PeriodOption getPeriodOption() {
		return periodOption;
	}



	public void setPeriodOption(PeriodOption periodOption) {
		this.periodOption = periodOption;
	}



	public int getPotential_points() {
		return potential_points;
	}



	public void setPotential_points(int potential_points) {
		this.potential_points = potential_points;
	}



	public int getAwarded_points() {
		return awarded_points;
	}



	public void setAwarded_points(int awarded_points) {
		this.awarded_points = awarded_points;
	}



	public boolean isCashed_out() {
		return cashed_out;
	}



	public void setCashed_out(boolean cashed_out) {
		this.cashed_out = cashed_out;
	}



	public boolean isProcessed() {
		return processed;
	}



	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	
	
}
