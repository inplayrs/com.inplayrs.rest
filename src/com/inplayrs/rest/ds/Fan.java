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
@Table(name = "fan")
public class Fan {

	@Id
	@Column(name = "fan_id")
	@GeneratedValue
	private int fan_id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = false)
	private User user;
	
	@JsonIgnore
	@Column(name = "user", insertable=false, updatable = false)
	private Integer user_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fangroup", nullable = false)
	private FanGroup fangroup;
	
	@JsonIgnore
	@Column(name = "fangroup", insertable=false, updatable = false)
	private int fangroup_id;
	
	@Column(name = "pat_count")
	private int pat_count;
	
	@Column(name = "winnings")
	private int winnings;
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public Fan() {
		
	}


	public int getFan_id() {
		return fan_id;
	}


	public void setFan_id(int fan_id) {
		this.fan_id = fan_id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public FanGroup getFangroup() {
		return fangroup;
	}


	public void setFangroup(FanGroup fangroup) {
		this.fangroup = fangroup;
	}


	public int getPat_count() {
		return pat_count;
	}


	public void setPat_count(int pat_count) {
		this.pat_count = pat_count;
	}


	public int getWinnings() {
		return winnings;
	}


	public void setWinnings(int winnings) {
		this.winnings = winnings;
	}


	public int getFangroup_id() {
		return fangroup_id;
	}


	public void setFangroup_id(int fangroup_id) {
		this.fangroup_id = fangroup_id;
	}


	public Integer getUser_id() {
		return user_id;
	}


	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	
}
