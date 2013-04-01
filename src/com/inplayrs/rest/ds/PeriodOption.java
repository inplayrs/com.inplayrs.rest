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
@Table(name = "period_option")
public class PeriodOption {
	
	@Id
	@Column(name = "period_option_id")
	@GeneratedValue
	private int period_option_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "period", nullable = false)
	@JsonIgnore
	private Period period;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "current_points")
	private int current_points;
	
	@Column(name = "outcome")
	private int outcome;

	@Column(name = "processed")
	@JsonIgnore
	private boolean processed;
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public PeriodOption() {
	}


	public int getPeriod_option_id() {
		return period_option_id;
	}


	public void setPeriod_option_id(int period_option_id) {
		this.period_option_id = period_option_id;
	}


	public Period getPeriod() {
		return period;
	}


	public void setPeriod(Period period) {
		this.period = period;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getCurrent_points() {
		return current_points;
	}


	public void setCurrent_points(int current_points) {
		this.current_points = current_points;
	}


	public int getOutcome() {
		return outcome;
	}


	public void setOutcome(int outcome) {
		this.outcome = outcome;
	}


	public boolean isProcessed() {
		return processed;
	}


	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	

}
