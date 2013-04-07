package com.inplayrs.rest.responseds;


import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.ds.Game;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;

public class GameResponse {

	@JsonProperty
	private int game_id;
	
	@JsonProperty
	private String name;
	
	@JsonProperty
	private int category_id;

	@JsonProperty
	private int competition_id;
	
	@JsonProperty
	private int game_type;
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime start_date;
	
	@JsonProperty
	private int state;
	
	@JsonProperty
	private int stake;
	
	@JsonProperty
	private int num_user_entries;
	
	@JsonProperty
	private int global_pot_size;

	@JsonProperty
	private int fangroup_pot_size;

	
	public GameResponse(Game g) {
		this.game_id = g.getGame_id();
		this.name = g.getName();
		this.category_id = g.getCompetition().getCategory_id();
		this.competition_id = g.getCompetition_id();
		this.game_type = g.getGame_type();
		this.start_date = g.getStart_date();
		this.state = g.getState();
		this.stake = g.getStake();	
		this.num_user_entries = g.getNum_user_entries();
		this.global_pot_size = g.getGlobal_pot_size();
		this.fangroup_pot_size = g.getFangroup_pot_size();
		
	}

	
	
	
}
