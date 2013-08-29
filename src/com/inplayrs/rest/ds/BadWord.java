package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bad_words")
public class BadWord {

	@Id
	@Column(name = "word")
	private String word;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public BadWord() {
		
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	
}
