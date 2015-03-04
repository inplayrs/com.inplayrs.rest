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
@Table(name = "photo_flag")
public class PhotoFlag {

	@Id
	@Column(name = "flag_id")
	@GeneratedValue
	private Integer flag_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "photo", nullable = false)
	@JsonIgnore
	private Photo photo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = false)
	@JsonIgnore
	private User user;
	
	public PhotoFlag() {}

	public Integer getFlag_id() {
		return flag_id;
	}

	public void setFlag_id(Integer flag_id) {
		this.flag_id = flag_id;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
