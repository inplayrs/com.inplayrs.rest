package com.inplayrs.rest.responseds;

import java.util.List;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;

public class CompetitionWinnersResponse {

	@JsonProperty
	private Integer comp_id;
	
	@JsonProperty
	private String competition;
	
	@JsonProperty
	private Integer category_id;
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime compEndDate;
	
	@JsonProperty
	private List<String> winners;
	
	
	/*
	 * Constructor
	 */
	public CompetitionWinnersResponse() {
		
	}


	public Integer getComp_id() {
		return comp_id;
	}

	public void setComp_id(Integer comp_id) {
		this.comp_id = comp_id;
	}


	public String getCompetition() {
		return competition;
	}


	public void setCompetition(String competition) {
		this.competition = competition;
	}


	public Integer getCategory_id() {
		return category_id;
	}


	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}


	public LocalDateTime getCompEndDate() {
		return compEndDate;
	}


	public void setCompEndDate(LocalDateTime compEndDate) {
		this.compEndDate = compEndDate;
	}


	public List<String> getWinners() {
		return winners;
	}


	public void setWinners(List<String> winners) {
		this.winners = winners;
	}

	
}
