package com.inplayrs.rest.responseds;

import java.util.Set;

import org.joda.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonProperty;
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
	
	@JsonProperty
	private int game_id;
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime start_date;

	@JsonProperty
	private String elapsed_time;
	
	@JsonProperty
	private int state;
	
	@JsonProperty
	private String score;
	
	@JsonProperty
	private String result;
	
	@JsonProperty
	private int homePoints;
	
	@JsonProperty
	private int drawPoints;
	
	@JsonProperty
	private int awayPoints;
	
	public PeriodResponse(Period p) {
		this.period_id = p.getPeriod_id();
		this.name = p.getName();
		this.game_id = p.getGame().getGame_id();
		this.start_date = p.getStart_date();
		this.elapsed_time = p.getElapsed_time();
		this.state = p.getState();
		this.score = p.getScore();
		this.result = p.getResult();
		
		// Set home, draw & away points
		for (PeriodOption po : p.getPeriodOptions()) {	
			String option_name = po.getName().toLowerCase();
			
			if (option_name.equals("home")) {
				this.homePoints = po.getCurrent_points();
			} 
			else if (option_name.equals("draw")) {
				this.drawPoints = po.getCurrent_points();
			} 
			else if (option_name.equals("away")) {
				this.awayPoints = po.getCurrent_points();
			}
			
		}
		
	}

	
}
