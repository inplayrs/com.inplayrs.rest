package com.inplayrs.rest.responseds;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inplayrs.rest.ds.FanGroup;

public class FangroupResponse {


	@JsonProperty
	private int fangroup_id;
	
	@JsonProperty
	private String fangroup_name;
	
	@JsonProperty
	private String competition_name;
	
	@JsonProperty
	private int category_id;
	
	
	public FangroupResponse(FanGroup f) {
		this.fangroup_id = f.getFangroup_id();
		this.fangroup_name = f.getName();
		this.competition_name = f.getCompetition().getName();
		this.category_id = f.getCompetition().getCategory_id();
	}
	
	
}
