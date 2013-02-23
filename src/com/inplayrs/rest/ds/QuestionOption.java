package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "question_option")
public class QuestionOption {

	@Id
	@Column(name = "option_id")
	@GeneratedValue
	@JsonIgnore
	private int question_option_id;
	
	@Column(name = "template_question")
	@JsonIgnore
	private int template_question_id;
	
	@Column(name = "name")
	private String name;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public QuestionOption() {
	}

	public int getQuestion_option_id() {
		return question_option_id;
	}

	public void setQuestion_option_id(int question_option_id) {
		this.question_option_id = question_option_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTemplate_question_id() {
		return template_question_id;
	}

	public void setTemplate_question_id(int template_question_id) {
		this.template_question_id = template_question_id;
	}
	
	
	
	
	
	
}
