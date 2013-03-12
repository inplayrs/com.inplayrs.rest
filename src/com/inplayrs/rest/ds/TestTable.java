package com.inplayrs.rest.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inplayrs.rest.jodatime.CustomDateTimeDeserializer;
import com.inplayrs.rest.jodatime.CustomDateTimeSerializer;

@Entity
@Table(name = "test_table")
public class TestTable {

	
	@Id
	@Column(name = "id")
	@GeneratedValue
	private int id;
	
	@Column(name = "field_1_var")
	private String field_1_var;
	
	@Column(name = "field_2_int")
	private int field_2_int;
	
	
	@Column(name = "field_3_date")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime field_3_date;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public TestTable() {
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getField_1_var() {
		return field_1_var;
	}


	public void setField_1_var(String field_1_var) {
		this.field_1_var = field_1_var;
	}


	public int getField_2_int() {
		return field_2_int;
	}


	public void setField_2_int(int field_2_int) {
		this.field_2_int = field_2_int;
	}


	public LocalDateTime getField_3_date() {
		return field_3_date;
	}


	public void setField_3_date(LocalDateTime field_3_date) {
		this.field_3_date = field_3_date;
	}
	
	
	
}
