package com.inplayrs.rest.responseds;

import javax.persistence.Column;
import javax.persistence.Entity;



import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
	
	@Column(name = "num_players")
	@JsonProperty
	private int num_players;

	@Column(name = "entered")
	@JsonProperty
	private boolean entered;  // has the current user entered this game
	
	@Column(name = "banner_position")
	@JsonProperty
	@JsonInclude(Include.NON_NULL) 
	private Integer banner_position;
	
	@Column(name = "banner_image_url")
	@JsonProperty
	@JsonInclude(Include.NON_NULL) 
	private String banner_image_url;
	
	@Column(name = "inplay_type")
	@JsonProperty
	private Integer inplay_type;
	
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



	public int getNum_players() {
		return num_players;
	}



	public void setNum_players(int num_players) {
		this.num_players = num_players;
	}


	public boolean isEntered() {
		return entered;
	}


	public void setEntered(boolean entered) {
		this.entered = entered;
	}


	public Integer getBanner_position() {
		return banner_position;
	}


	public void setBanner_position(Integer banner_position) {
		this.banner_position = banner_position;
	}


	public String getBanner_image_url() {
		return banner_image_url;
	}


	public void setBanner_image_url(String banner_image_url) {
		this.banner_image_url = banner_image_url;
	}


	public Integer getInplay_type() {
		return inplay_type;
	}


	public void setInplay_type(Integer inplay_type) {
		this.inplay_type = inplay_type;
	}

	
}
