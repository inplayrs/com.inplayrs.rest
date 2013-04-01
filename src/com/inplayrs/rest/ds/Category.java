package com.inplayrs.rest.ds;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

	@Id
	@Column(name = "cat_id")
	@GeneratedValue
	private int cat_id;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	private Set <Competition> competitions;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public Category() {
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Competition> getCompetitions() {
		return competitions;
	}

	public void setCompetitions(Set<Competition> competitions) {
		this.competitions = competitions;
	}
	
	
	
	
	
}
