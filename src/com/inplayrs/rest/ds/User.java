package com.inplayrs.rest.ds;

public class User {

	private int id;
	private String username;
	private String email;
	private String first_name;
	private String last_name;
	private int pat_count;
	
	
	public User(int id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}
	
	
	
	public User(int id, String username, String email, String first_name, String last_name) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;	
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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


	public String getFirst_name() {
		return first_name;
	}


	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}


	public String getLast_name() {
		return last_name;
	}


	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	public int getPat_count() {
		return pat_count;
	}


	public void setPat_count(int pat_count) {
		this.pat_count = pat_count;
	}
	
	
	
	
	
	
}
