package com.inplayrs.rest.ds;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "fangroup")
public class FanGroup {

	@Id
	@Column(name = "fangroup_id")
	@GeneratedValue
	private int fangroup_id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "competition", nullable = false)
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private Competition competition;
	
	@JsonIgnore
	@Column(name = "competition", insertable=false, updatable = false)
	private int comp_id;
	
	
	@Column(name = "pat_count")
	private int pat_count;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public FanGroup() {
	}

	public int getFangroup_id() {
		return fangroup_id;
	}

	public void setFangroup_id(int fangroup_id) {
		this.fangroup_id = fangroup_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public int getPat_count() {
		return pat_count;
	}

	public void setPat_count(int pat_count) {
		this.pat_count = pat_count;
	}

	public int getComp_id() {
		return comp_id;
	}

	public void setComp_id(int comp_id) {
		this.comp_id = comp_id;
	}

	

}
