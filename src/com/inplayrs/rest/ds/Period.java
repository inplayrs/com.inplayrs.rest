package com.inplayrs.rest.ds;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
 

@Entity
@Table(name = "period")
public class Period {


	// Game variables
	/*
	private String game_name;
	private int game_template_id;
	private int event;
	private int round;
	private int game_state;
	 */
	
	// Period variables
	@Id
	@Column(name = "period_id")
	@GeneratedValue
	private int period_id;
	
	@Column(name = "game")
	private int game_id;
	
	@Column(name = "name")
	private String name;
	
	@Column(nullable=true, name = "state")
	private Integer state;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "period")
	private Set <PeriodOption> periodOptions;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public Period() {
	}
	
	

	public Period(int period_id, int game_id, String name, Integer state) {
		this.period_id = period_id;
		this.game_id = game_id;
		this.name = name;
		this.state = state;
	}



	public int getPeriod_id() {
		return period_id;
	}

	public void setPeriod_id(int period_id) {
		this.period_id = period_id;
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	public Set<PeriodOption> getPeriodOptions() {
		return this.periodOptions;
	}
	
	public void setPeriodOptions(Set<PeriodOption> periodOptions) {
		this.periodOptions = periodOptions;
	}
	
	
}
