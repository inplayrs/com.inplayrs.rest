package com.inplayrs.rest.ds;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;
 

@Entity
@Table(name = "period")
public class Period {


	// Period variables
	@Id
	@Column(name = "period_id")
	@GeneratedValue
	private int period_id;
	
	@Column(name = "game")
	private int game_id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "start_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime start_time;
	
	@Column(name = "elapsed_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime elapsed_time;
	
	@Column(nullable=true, name = "state")
	private Integer state;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "period")
	private Set <PeriodOption> periodOptions;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public Period() {
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


	public LocalDateTime getStart_time() {
		return start_time;
	}


	public void setStart_time(LocalDateTime start_time) {
		this.start_time = start_time;
	}


	public LocalDateTime getElapsed_time() {
		return elapsed_time;
	}


	public void setElapsed_time(LocalDateTime elapsed_time) {
		this.elapsed_time = elapsed_time;
	}
	
	
}
