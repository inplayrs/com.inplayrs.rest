package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "game_stake")
public class GameStake {

	@Id
	@Column(name = "stake_id")
	@GeneratedValue
	private int stake_id;
	
	@Column(name = "game_template")
	private int game_template_id;
	
	@Column(name = "amount")
	private float amount;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public GameStake() {
		
	}

	public int getStake_id() {
		return stake_id;
	}

	public void setStake_id(int stake_id) {
		this.stake_id = stake_id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getGame_template_id() {
		return game_template_id;
	}

	public void setGame_template_id(int game_template_id) {
		this.game_template_id = game_template_id;
	}
	
	
	
	
	
}
