package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_trophy")
public class UserTrophy {

	@Id
	@Column(name = "user_trophy_id")
	@GeneratedValue
	private int userTrophyId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = false)
	@JsonIgnore
	private User user;
	
	@Column(name = "user", insertable=false, updatable = false)
	private Integer user_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "trophy", nullable = false)
	@JsonIgnore
	private Trophy trophy;
	
	public UserTrophy() {
		
	}

	public int getUserTrophyId() {
		return userTrophyId;
	}

	public void setUserTrophyId(int userTrophyId) {
		this.userTrophyId = userTrophyId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Trophy getTrophy() {
		return trophy;
	}

	public void setTrophy(Trophy trophy) {
		this.trophy = trophy;
	}
	
	
	
}
