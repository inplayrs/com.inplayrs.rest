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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "game_entry")
public class GameEntry {

	@Id
	@Column(name = "game_entry_id")
	@GeneratedValue
	private int game_entry_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game", nullable = false)
	@JsonIgnore
	private Game game;
	
	@Column(name = "game", insertable=false, updatable = false)
	private int game_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = false)
	@JsonIgnore
	private User user;
	
	@Column(name = "user", insertable=false, updatable = false)
	private Integer user_id;
	
	@Column(name = "total_points")
	private int total_points;
	
	@Column(name = "global_winnings")
	private int global_winnings;
	
	@Column(name = "fangroup_winnings")
	private int fangroup_winnings;
	
	@Column(name = "h2h_winnings")
	private int h2h_winnings;
	
	@Column(name = "total_winnings")
	private int total_winnings;
	
	@Column(name = "entry_state")
	@JsonIgnore
	private Integer entry_state;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "gameEntry")
	@Cascade({CascadeType.DELETE})
	private Set <PeriodSelection> periodSelections;
	
	
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public GameEntry() {
		
	}
	
	
	public GameEntry(int game_id, int user_id) {
		this.game_id = game_id;
		this.user_id = user_id;
	}


	public int getGame_entry_id() {
		return game_entry_id;
	}


	public void setGame_entry_id(int game_entry_id) {
		this.game_entry_id = game_entry_id;
	}


	public Game getGame() {
		return game;
	}


	public void setGame(Game game) {
		this.game = game;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public int getTotal_points() {
		return total_points;
	}


	public void setTotal_points(int total_points) {
		this.total_points = total_points;
	}


	public int getGlobal_winnings() {
		return global_winnings;
	}


	public void setGlobal_winnings(int global_winnings) {
		this.global_winnings = global_winnings;
	}


	public int getFangroup_winnings() {
		return fangroup_winnings;
	}


	public void setFangroup_winnings(int fangroup_winnings) {
		this.fangroup_winnings = fangroup_winnings;
	}


	public int getH2h_winnings() {
		return h2h_winnings;
	}


	public void setH2h_winnings(int h2h_winnings) {
		this.h2h_winnings = h2h_winnings;
	}


	public Set<PeriodSelection> getPeriodSelections() {
		return periodSelections;
	}


	public void setPeriodSelections(Set<PeriodSelection> periodSelections) {
		this.periodSelections = periodSelections;
	}


	public int getTotal_winnings() {
		return total_winnings;
	}


	public void setTotal_winnings(int total_winnings) {
		this.total_winnings = total_winnings;
	}


	public int getGame_id() {
		return game_id;
	}


	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}


	public Integer getUser_id() {
		return user_id;
	}


	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}


	public Integer getEntry_state() {
		return entry_state;
	}


	public void setEntry_state(Integer entry_state) {
		this.entry_state = entry_state;
	}

	
}
