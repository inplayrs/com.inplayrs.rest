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
	
	
	
}
