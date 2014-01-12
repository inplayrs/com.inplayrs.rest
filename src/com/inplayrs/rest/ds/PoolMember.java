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
@Table(name = "pool_member")
public class PoolMember {

	@Id
	@Column(name = "pool_member_id")
	@GeneratedValue
	private int poolMemberID;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pool", nullable = false)
	private Pool pool;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = false)
	private User user;
	
	
	public PoolMember() {
		
	}


	public int getPoolMemberID() {
		return poolMemberID;
	}


	public void setPoolMemberID(int poolMemberID) {
		this.poolMemberID = poolMemberID;
	}


	public Pool getPool() {
		return pool;
	}


	public void setPool(Pool pool) {
		this.pool = pool;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
}
