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

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pool")
public class Pool implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "pool_id")
	@GeneratedValue
	private Integer pool_id;
	
	@Column(name = "name", unique=true)
	private String name;
	
	@Generated(GenerationTime.ALWAYS)
	@Column(name = "num_players", insertable=false, updatable=false)
	@JsonIgnore
	private Integer num_players;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by", nullable = false)
	@JsonIgnore
	private User created_by;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pool")
	@JsonIgnore
	private Set <PoolMember> poolMembers;
	
	/*
	 * Default constructor - required by Hibernate
	 */
	public Pool() {
		
	}

	public Integer getPool_id() {
		return pool_id;
	}

	public void setPool_id(Integer pool_id) {
		this.pool_id = pool_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum_players() {
		return num_players;
	}

	public void setNum_players(Integer num_players) {
		this.num_players = num_players;
	}

	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public Set<PoolMember> getPoolMembers() {
		return poolMembers;
	}

	public void setPoolMembers(Set<PoolMember> poolMembers) {
		this.poolMembers = poolMembers;
	}

	
}
