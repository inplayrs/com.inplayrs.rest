package com.inplayrs.rest.ds;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "selection_combination")
public class SelectionCombination {
	
	@Id
	@Column(name = "combination_id")
	@GeneratedValue
	private int combination_id;
	
	@Column(name = "points")
	private int points;

	
	@OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="combination_options",
            joinColumns = @JoinColumn( name="selection_combination"),
            inverseJoinColumns = @JoinColumn( name="period_option")
    )
	private Set <PeriodOption> periodOptions;
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public SelectionCombination() {
		
	}


	public int getCombination_id() {
		return combination_id;
	}


	public void setCombination_id(int combination_id) {
		this.combination_id = combination_id;
	}


	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}


	public Set<PeriodOption> getPeriodOptions() {
		return periodOptions;
	}


	public void setPeriodOptions(Set<PeriodOption> periodOptions) {
		this.periodOptions = periodOptions;
	}
	
	
	
	
}
