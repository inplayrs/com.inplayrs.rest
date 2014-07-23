package com.inplayrs.rest.responseds;

import java.util.Set;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.PeriodOption;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;

public class PeriodResponse {

	@JsonProperty
	private int period_id;
	
	@JsonProperty
	private String name;
		
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime start_date;
	
	@JsonProperty
	private String elapsed_time;
	
	@JsonProperty
	private int state;
	
	@JsonProperty
	private int game_state;

	@JsonProperty
	private String score;
	
	@JsonProperty
	private int result;
	
	@JsonProperty
	private int points0;
	
	@JsonProperty
	private int points1;
	
	@JsonProperty
	private int points2;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL) 
	private Set<PeriodOption> periodOptions;
	
	
	public PeriodResponse(Period p) {
		this.period_id = p.getPeriod_id();
		this.name = p.getName();
		this.start_date = p.getStart_date();
		this.elapsed_time = p.getElapsed_time();
		this.state = p.getState();
		this.game_state = p.getGame().getState();
		this.score = p.getScore();
		this.result = p.getResult();
		this.points0 = p.getPoints0();
		this.points1 = p.getPoints1();
		this.points2 = p.getPoints2();
		this.periodOptions = p.getPeriodOptions();
	}

	
}
