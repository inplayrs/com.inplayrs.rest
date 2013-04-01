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


import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;


@Entity
@Table(name = "period")
public class Period {

	@Id
	@Column(name = "period_id")
	@GeneratedValue
	private int period_id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game", nullable = false)
	@JsonIgnore
	private Game game;
	
	@Column(name = "start_date")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime start_date;
	
	@Column(name = "end_date")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime end_date;
	
	@Column(name = "elapsed_time")
	private String elapsed_time;
	
	@Column(name = "state")
	private int state;

	@Column(name = "score")
	private String score;
	
	@Column(name = "result")
	private String result;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "period")
	private Set<PeriodOption> periodOptions;
	
	
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Game getGame() {
		return game;
	}


	public void setGame(Game game) {
		this.game = game;
	}


	public LocalDateTime getStart_date() {
		return start_date;
	}


	public void setStart_date(LocalDateTime start_date) {
		this.start_date = start_date;
	}


	public LocalDateTime getEnd_date() {
		return end_date;
	}


	public void setEnd_date(LocalDateTime end_date) {
		this.end_date = end_date;
	}


	public String getElapsed_time() {
		return elapsed_time;
	}


	public void setElapsed_time(String elapsed_time) {
		this.elapsed_time = elapsed_time;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public String getScore() {
		return score;
	}


	public void setScore(String score) {
		this.score = score;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public Set<PeriodOption> getPeriodOptions() {
		return periodOptions;
	}


	public void setPeriodOptions(Set<PeriodOption> periodOptions) {
		this.periodOptions = periodOptions;
	}

	
}
