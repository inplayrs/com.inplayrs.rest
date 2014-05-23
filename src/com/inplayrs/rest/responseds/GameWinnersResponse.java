package com.inplayrs.rest.responseds;

import java.util.List;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;

public class GameWinnersResponse {

	@JsonProperty
	private Integer game_id;
	
	@JsonProperty
	private String game;
	
	@JsonProperty
	private Integer category_id;
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime gameEndDate;
	
	@JsonProperty
	private Integer comp_id;
	
	@JsonProperty
	private Integer inplay_type;
	
	@JsonProperty
	private Integer state;
	
	
	@JsonProperty
	private List<String> winners;
	
	
	public GameWinnersResponse () {
		
	}


	public Integer getGame_id() {
		return game_id;
	}


	public void setGame_id(Integer game_id) {
		this.game_id = game_id;
	}


	public String getGame() {
		return game;
	}


	public void setGame(String game) {
		this.game = game;
	}


	public Integer getCategory_id() {
		return category_id;
	}


	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}


	public LocalDateTime getGameEndDate() {
		return gameEndDate;
	}


	public void setGameEndDate(LocalDateTime gameEndDate) {
		this.gameEndDate = gameEndDate;
	}


	public List<String> getWinners() {
		return winners;
	}


	public void setWinners(List<String> winners) {
		this.winners = winners;
	}


	public Integer getComp_id() {
		return comp_id;
	}


	public void setComp_id(Integer comp_id) {
		this.comp_id = comp_id;
	}


	public Integer getInplay_type() {
		return inplay_type;
	}


	public void setInplay_type(Integer inplay_type) {
		this.inplay_type = inplay_type;
	}


	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
}
