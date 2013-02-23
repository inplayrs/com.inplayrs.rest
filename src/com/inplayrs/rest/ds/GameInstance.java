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
@Table(name = "game_instance")
public class GameInstance {

	@Id
	@Column(name = "instance_id")
	@GeneratedValue
	private int instance_id;
	
	@Column(name = "game", insertable = false, updatable = false)
	private int game_id;
	
	@Column(name = "stake", insertable = false, updatable = false)
	private int stake_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stake", nullable = false)
	private GameStake gameStake;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game", nullable = false)
	private Game game;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public GameInstance() {
		
	}

	public int getInstance_id() {
		return instance_id;
	}

	public void setInstance_id(int instance_id) {
		this.instance_id = instance_id;
	}

	public GameStake getGameStake() {
		return gameStake;
	}

	public void setGameStake(GameStake gameStake) {
		this.gameStake = gameStake;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public int getStake_id() {
		return stake_id;
	}

	public void setStake_id(int stake_id) {
		this.stake_id = stake_id;
	}
	
	
}
