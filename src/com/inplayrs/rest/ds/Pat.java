package com.inplayrs.rest.ds;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

@SuppressWarnings("serial")
@Entity
@Table(name = "pat")
public class Pat implements Serializable {

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "from_user", nullable = false)
	private User fromUser;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "to_user", nullable = false)
	private User toUser;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "competition", nullable = false)
	private Competition competition;
	
	@Id
	@Column(name = "created")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime created;
	
	public Pat() {
		
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((competition == null) ? 0 : competition.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((fromUser == null) ? 0 : fromUser.hashCode());
		result = prime * result + ((toUser == null) ? 0 : toUser.hashCode());
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
		Pat other = (Pat) obj;
		if (competition == null) {
			if (other.competition != null)
				return false;
		} else if (!competition.equals(other.competition))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (fromUser == null) {
			if (other.fromUser != null)
				return false;
		} else if (!fromUser.equals(other.fromUser))
			return false;
		if (toUser == null) {
			if (other.toUser != null)
				return false;
		} else if (!toUser.equals(other.toUser))
			return false;
		return true;
	}

	
	
}
