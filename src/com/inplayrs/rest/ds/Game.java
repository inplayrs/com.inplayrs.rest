package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "game")
public class Game {

	@Id
	@Column(name = "game_id")
	@GeneratedValue
	private int game_id;
		
	@Column(name = "name")
	private String name;
	
	@Column(name = "state")
	private int state;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game_template", nullable = false)
	private GameTemplate gameTemplate;
	
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public GameTemplate getGameTemplate() {
		return gameTemplate;
	}

	public void setGameTemplate(GameTemplate gameTemplate) {
		this.gameTemplate = gameTemplate;
	}
	
	
	
	
	
}
