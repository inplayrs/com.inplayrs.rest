package com.inplayrs.rest.ds;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@SuppressWarnings("serial")
@Entity
@Table(name = "user")
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "user_id")
	private int user_id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "total_pat_count")
	private int total_pat_count;
	
	@Column(name = "total_winnings")
	private int total_winnings;
	
	@Column(name = "password_hash")
	@JsonIgnore
	private String password_hash;
	
	@Column(name = "timezone")
	@JsonIgnore
	private String timezone;
	
	@Column(name = "device_id")
	@JsonIgnore
	private String deviceID;
	
	@Column(name = "push_active")
	@JsonIgnore
	private boolean pushActive;
	
	@Column(name = "gamecenter_id")
	@JsonIgnore
	private String gamecenter_id;
	
	@Column(name = "gamecenter_username")
	@JsonIgnore
	private String gamecenter_username;
	
	@Column(name = "facebook_id")
	@JsonIgnore
	private String facebook_id;
	
	@Column(name = "facebook_username")
	@JsonIgnore
	private String facebook_username;
	
	@Column(name = "facebook_full_name")
	@JsonIgnore
	private String facebook_full_name;
	
	@Column(name = "facebook_email")
	@JsonIgnore
	private String facebook_email;
	
	@Column(name = "is_bot")
	@JsonIgnore
	private boolean bot;
	
	
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


	public String getPassword_hash() {
		return password_hash;
	}


	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}
	
	
	public String getTimezone() {
		return timezone;
	}


	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}


	public String getDeviceID() {
		return deviceID;
	}


	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	
	public boolean isPushActive() {
		return pushActive;
	}


	public void setPushActive(boolean pushActive) {
		this.pushActive = pushActive;
	}
	

	public String getGamecenter_id() {
		return gamecenter_id;
	}


	public void setGamecenter_id(String gamecenter_id) {
		this.gamecenter_id = gamecenter_id;
	}


	public String getGamecenter_username() {
		return gamecenter_username;
	}


	public void setGamecenter_username(String gamecenter_username) {
		this.gamecenter_username = gamecenter_username;
	}


	public String getFacebook_id() {
		return facebook_id;
	}


	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}


	public String getFacebook_username() {
		return facebook_username;
	}


	public void setFacebook_username(String facebook_username) {
		this.facebook_username = facebook_username;
	}


	public String getFacebook_full_name() {
		return facebook_full_name;
	}


	public void setFacebook_full_name(String facebook_full_name) {
		this.facebook_full_name = facebook_full_name;
	}


	public String getFacebook_email() {
		return facebook_email;
	}


	public void setFacebook_email(String facebook_email) {
		this.facebook_email = facebook_email;
	}


	public boolean isBot() {
		return bot;
	}


	public void setBot(boolean bot) {
		this.bot = bot;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("user_id: ");
		str.append(this.getUser_id());
		str.append(", username: ").append(this.getUsername());
		str.append(", email: ").append(this.getEmail());
		return str.toString();
	}
	
	
}
