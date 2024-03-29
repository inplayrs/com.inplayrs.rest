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
@Table(name = "game")
public class Game {

	@Id
	@Column(name = "game_id")
	@GeneratedValue
	private int game_id;
	
	@Column(name = "name")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "competition", nullable = false)
	@JsonIgnore
	private Competition competition;
	
	@Column(name = "competition", insertable=false, updatable = false)
	private int competition_id;
	
	@Column(name = "game_type")
	private int game_type;
	
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
	
	@Column(name = "state")
	private int state;
	
	@Column(name = "stake")
	private int stake;
	
	@Column(name = "num_players")
	private int num_players;
	
	@Column(name = "global_pot_size")
	private int global_pot_size;

	@Column(name = "fangroup_pot_size")
	private int fangroup_pot_size;
	
	@Column(name = "processed")
	@JsonIgnore
	private boolean processed;
	
	@Column(name = "banner_position")
	private Integer bannerPosition;
	
	@Column(name = "banner_image_url")
	private String banner_image_url;
	
	@Column(name = "hidden")
	@JsonIgnore
	private boolean hidden;
	
	@Column(name = "inplay_type")
	private Integer inplay_type;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
	@JsonIgnore
	private Set<GameEntry> gameEntries;
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public Game() {
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



	public Competition getCompetition() {
		return competition;
	}



	public void setCompetition(Competition competition) {
		this.competition = competition;
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



	public void setStart_date(LocalDateTime start_date) {
		this.start_date = start_date;
	}



	public LocalDateTime getEnd_date() {
		return end_date;
	}



	public void setEnd_date(LocalDateTime end_date) {
		this.end_date = end_date;
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



	public boolean isProcessed() {
		return processed;
	}



	public void setProcessed(boolean processed) {
		this.processed = processed;
	}



	public int getStake() {
		return stake;
	}



	public void setStake(int stake) {
		this.stake = stake;
	}



	public int getCompetition_id() {
		return competition_id;
	}



	public void setCompetition_id(int competition_id) {
		this.competition_id = competition_id;
	}



	public Set<GameEntry> getGameEntries() {
		return gameEntries;
	}



	public void setGameEntries(Set<GameEntry> gameEntries) {
		this.gameEntries = gameEntries;
	}



	public Integer getBannerPosition() {
		return bannerPosition;
	}



	public void setBannerPosition(Integer banner_position) {
		this.bannerPosition = banner_position;
	}



	public String getBanner_image_url() {
		return banner_image_url;
	}



	public void setBanner_image_url(String banner_image_url) {
		this.banner_image_url = banner_image_url;
	}


	public boolean isHidden() {
		return hidden;
	}


	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}



	public Integer getInplay_type() {
		return inplay_type;
	}



	public void setInplay_type(Integer inplay_type) {
		this.inplay_type = inplay_type;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + game_id;
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (game_id != other.game_id)
			return false;
		return true;
	}

	
	
}
