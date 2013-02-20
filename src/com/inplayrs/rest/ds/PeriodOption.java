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
@Table(name = "period_option")
public class PeriodOption {
	
	@Id
	@Column(name = "period_option_id")
	@GeneratedValue
	private int period_option_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "period", nullable = false)
	@JsonIgnore
	private Period period;
	
	@Column(name = "event")
	private int event_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "question_option", nullable = false)
	private QuestionOption questionOption;
	
	@Column(nullable=true, name = "odds")
	private String odds;
	
	@Column(nullable=true, name = "points")
	private Integer points;
	
	@Column(nullable=true, name = "status")
	private Integer status;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public PeriodOption() {
	}

	
	/*
	 * Getters and Setters
	 */
	public int getPeriod_option_id() {
		return period_option_id;
	}

	public void setPeriod_option_id(int period_option_id) {
		this.period_option_id = period_option_id;
	}

	public Period getPeriod() {
		return this.period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public QuestionOption getQuestionOption() {
		return questionOption;
	}

	public void setQuestionOption(QuestionOption questionOption) {
		this.questionOption = questionOption;
	}

	public String getOdds() {
		return odds;
	}

	public void setOdds(String odds) {
		this.odds = odds;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	
	
	

}
