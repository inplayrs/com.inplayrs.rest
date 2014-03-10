package com.inplayrs.rest.responseds;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class PoolPointsResponse {
	
	@Column(name = "pool_rank")
	@JsonProperty
	private Integer pool_rank;
	
	@Column(name = "points")
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	private Integer points;
	
	@Column(name = "pool_size")
	@JsonProperty
	private Integer pool_size;
	
	@Column(name = "pool_pot_size")
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	private Integer pool_pot_size;
	
	@Column(name = "pool_winnings")
	@JsonProperty
	private Integer pool_winnings;
	
	@Column(name = "total_pool_winnings")
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	private Integer total_pool_winnings;
	
	
	public PoolPointsResponse() {
		
	}


	public Integer getPool_rank() {
		return pool_rank;
	}


	public void setPool_rank(Integer pool_rank) {
		this.pool_rank = pool_rank;
	}


	public Integer getPoints() {
		return points;
	}


	public void setPoints(Integer points) {
		this.points = points;
	}


	public Integer getPool_size() {
		return pool_size;
	}


	public void setPool_size(Integer pool_size) {
		this.pool_size = pool_size;
	}


	public Integer getPool_pot_size() {
		return pool_pot_size;
	}


	public void setPool_pot_size(Integer pool_pot_size) {
		this.pool_pot_size = pool_pot_size;
	}


	public Integer getPool_winnings() {
		return pool_winnings;
	}


	public void setPool_winnings(Integer pool_winnings) {
		this.pool_winnings = pool_winnings;
	}


	public Integer getTotal_pool_winnings() {
		return total_pool_winnings;
	}


	public void setTotal_pool_winnings(Integer total_pool_winnings) {
		this.total_pool_winnings = total_pool_winnings;
	}
	
	
	
}
