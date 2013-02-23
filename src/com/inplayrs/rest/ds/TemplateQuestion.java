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
@Table(name = "template_question")
public class TemplateQuestion {

	
	@Id
	@Column(name = "question_id")
	@GeneratedValue
	private int question_id;
	
	@Column(name = "game_template")
	private int game_template_id;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "template_question_id")
	private Set <QuestionOption> questionOptions;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public TemplateQuestion() {
		
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<QuestionOption> getQuestionOptions() {
		return questionOptions;
	}

	public void setQuestionOptions(Set<QuestionOption> questionOptions) {
		this.questionOptions = questionOptions;
	}

	public int getGame_template_id() {
		return game_template_id;
	}

	public void setGame_template_id(int game_template_id) {
		this.game_template_id = game_template_id;
	}
	
	
	
}
