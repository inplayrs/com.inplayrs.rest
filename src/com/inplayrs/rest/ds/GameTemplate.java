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
@Table(name = "game_template")
public class GameTemplate {

	@Id
	@Column(name = "game_template_id")
	@GeneratedValue
	private int game_template_id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "game_template_id")
	private Set <TemplateQuestion> templateQuestions;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "game_template_id")
	private Set <GameStake> gameStakes;
	
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public GameTemplate() {
		
	}

	public int getGame_template_id() {
		return game_template_id;
	}

	public void setGame_template_id(int game_template_id) {
		this.game_template_id = game_template_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<TemplateQuestion> getTemplateQuestions() {
		return templateQuestions;
	}

	public void setTemplateQuestions(Set<TemplateQuestion> templateQuestions) {
		this.templateQuestions = templateQuestions;
	}

	public Set<GameStake> getGameStakes() {
		return gameStakes;
	}

	public void setGameStakes(Set<GameStake> gameStakes) {
		this.gameStakes = gameStakes;
	}
	
	
	
}
