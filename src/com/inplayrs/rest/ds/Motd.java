package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;


@Entity
@Table(name = "motd")
public class Motd {
	
	@Id
	@Column(name = "motd_id")
	@GeneratedValue
	private int motd_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = false)
	private User user;
	
	@Column(name = "message")
	private String message;

	@Column(name = "processed")
	private Integer processed;

	@Column(name = "updated")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime updated;
	
	public Motd() {}


	public int getMotd_id() {
		return motd_id;
	}


	public void setMotd_id(int motd_id) {
		this.motd_id = motd_id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Integer getProcessed() {
		return processed;
	}


	public void setProcessed(Integer processed) {
		this.processed = processed;
	}


	public LocalDateTime getUpdated() {
		return updated;
	}


	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
	
	
	
	
}
