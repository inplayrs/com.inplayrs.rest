package com.inplayrs.rest.ds;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;

@SuppressWarnings("serial")
@Entity
@Table(name = "competition")
public class Competition implements Serializable {

	@Id
	@Column(name = "comp_id")
	@GeneratedValue
	private int comp_id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category", nullable = false)
	@JsonIgnore
	private Category category;
	
	@Column(name = "category", insertable=false, updatable = false)
	private int category_id;
	
	@Column(name = "start_date")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime start_date;
	
	@Column(name = "end_date")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime end_date;
	
	@Column(name = "state")
	private int state;
	
	@Column(name = "hidden")
	@JsonIgnore
	private boolean hidden;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "competition")
	@JsonIgnore
	private Set <Game> games;
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public Competition() {
	}


	public int getComp_id() {
		return comp_id;
	}


	public void setComp_id(int comp_id) {
		this.comp_id = comp_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public LocalDateTime getStart_date() {
		return start_date;
	}


	public void setStart_date(LocalDateTime start_date) {
		this.start_date = start_date;
	}


	public LocalDateTime getEnd_date() {
		return end_date;
	}


	public void setEnd_date(LocalDateTime end_date) {
		this.end_date = end_date;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public int getCategory_id() {
		return category_id;
	}


	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}


	public boolean isHidden() {
		return hidden;
	}


	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}


	public Set<Game> getGames() {
		return games;
	}


	public void setGames(Set<Game> games) {
		this.games = games;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + comp_id;
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
		Competition other = (Competition) obj;
		if (comp_id != other.comp_id)
			return false;
		return true;
	}
	
	
	
}
