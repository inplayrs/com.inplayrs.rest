package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue
	private int user_id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "total_pat_count")
	private int total_pat_count;
	
	@Column(name = "total_winnings")
	private int total_winnings;
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public User() {
		
	}


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getTotal_pat_count() {
		return total_pat_count;
	}


	public void setTotal_pat_count(int total_pat_count) {
		this.total_pat_count = total_pat_count;
	}


	public int getTotal_winnings() {
		return total_winnings;
	}


	public void setTotal_winnings(int total_winnings) {
		this.total_winnings = total_winnings;
	}

	
	
}
