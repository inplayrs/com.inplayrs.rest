package com.inplayrs.rest.responseds;

import javax.persistence.Column;
import javax.persistence.Entity;


import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;

@Entity
public class GameResponse {

	@Column(name = "game_id")
	@JsonProperty
	private int game_id;
	
	@Column(name = "name")
	@JsonProperty
	private String name;
	
	@Column(name = "category_id")
	@JsonProperty
	private int category_id;

	@Column(name = "competition_id")
	@JsonProperty
	private int competition_id;
	
	@Column(name = "game_type")
	@JsonProperty
	private int game_type;
	
	@Column(name = "start_date")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime start_date;
	
	@Column(name = "state")
	@JsonProperty
	private int state;
	
	@Column(name = "stake")
	@JsonProperty
	private int stake;
	
	@Column(name = "num_players")
	@JsonProperty
	private int num_players;
	
	@Column(name = "global_pot_size")
	@JsonProperty
	private int global_pot_size;

	@Column(name = "fangroup_pot_size")
	@JsonProperty
	private int fangroup_pot_size;

	@Column(name = "entered")
	@JsonProperty
	private boolean entered;  // has the current user entered this game
	
	/*
	 * Default constructor
	 */
	public GameResponse() {
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



	public int getCategory_id() {
		return category_id;
	}



	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}



	public int getCompetition_id() {
		return competition_id;
	}



	public void setCompetition_id(int competition_id) {
		this.competition_id = competition_id;
	}



	public int getGame_type() {
		return game_type;
	}



	public void setGame_type(int game_type) {
		this.game_type = game_type;
	}



	public LocalDateTime getStart_date() {
		return start_date;
	}


	/*
	 * Using java.sql.Timestamp as that is what is being
	 * returned from the hibernate query, and the mapping to 
	 * LocalDateTime does not seem to be working 
	 */
	public void setStart_date(java.sql.Timestamp start_date) {
		this.start_date = new LocalDateTime(start_date);
	}

	
	public int getState() {
		return state;
	}



	public void setState(int state) {
		this.state = state;
	}



	public int getStake() {
		return stake;
	}



	public void setStake(int stake) {
		this.stake = stake;
	}



	public int getNum_players() {
		return num_players;
	}



	public void setNum_players(int num_players) {
		this.num_players = num_players;
	}



	public int getGlobal_pot_size() {
		return global_pot_size;
	}



	public void setGlobal_pot_size(int global_pot_size) {
		this.global_pot_size = global_pot_size;
	}



	public int getFangroup_pot_size() {
		return fangroup_pot_size;
	}



	public void setFangroup_pot_size(int fangroup_pot_size) {
		this.fangroup_pot_size = fangroup_pot_size;
	}


	public boolean isEntered() {
		return entered;
	}


	public void setEntered(boolean entered) {
		this.entered = entered;
	}


	
}
