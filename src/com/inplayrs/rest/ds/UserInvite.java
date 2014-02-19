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
@Table(name = "user_invite")
public class UserInvite {

	@Id
	@Column(name = "user_invite_id")
	@GeneratedValue
	private int userInviteId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "source_user", nullable = false)
	private User sourceUser;
	
	@Column(name = "facebook_id")
	private String facebookId;
	
	@Column(name = "email")
	private String email;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pool", nullable = false)
	private Pool pool;
	
	
	public UserInvite() {
		
	}


	public int getUserInviteId() {
		return userInviteId;
	}


	public void setUserInviteId(int userInviteId) {
		this.userInviteId = userInviteId;
	}


	public User getSourceUser() {
		return sourceUser;
	}


	public void setSourceUser(User sourceUser) {
		this.sourceUser = sourceUser;
	}


	public String getFacebookId() {
		return facebookId;
	}


	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Pool getPool() {
		return pool;
	}


	public void setPool(Pool pool) {
		this.pool = pool;
	}
	
	
}
