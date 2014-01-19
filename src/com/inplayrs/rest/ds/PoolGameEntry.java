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
@Table(name = "pool_game_entry")
public class PoolGameEntry {

	@Id
	@Column(name = "pool_game_entry_id")
	@GeneratedValue
	private int poolGameEntryID;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pool_member", nullable = false)
	private PoolMember poolMember;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game_entry", nullable = false)
	private GameEntry gameEntry;
	
	
	public PoolGameEntry() {
		
	}


	public int getPoolGameEntryID() {
		return poolGameEntryID;
	}


	public void setPoolGameEntryID(int poolGameEntryID) {
		this.poolGameEntryID = poolGameEntryID;
	}


	public PoolMember getPoolMember() {
		return poolMember;
	}


	public void setPoolMember(PoolMember poolMember) {
		this.poolMember = poolMember;
	}


	public GameEntry getGameEntry() {
		return gameEntry;
	}


	public void setGameEntry(GameEntry gameEntry) {
		this.gameEntry = gameEntry;
	}
	
	
	
}
