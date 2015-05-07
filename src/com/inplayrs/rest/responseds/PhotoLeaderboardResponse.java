package com.inplayrs.rest.responseds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PhotoLeaderboardResponse {

	@JsonProperty
	private Integer rank;
	
	@JsonProperty
	private String username;
	
	@JsonProperty
	private String fbID;
	
	@JsonProperty
	private Integer photo_id;
	
	@JsonProperty
	private Integer likes;
	
	@JsonProperty
	private String url;
	
	@JsonProperty
	private String caption;
	
	@JsonProperty
	private Integer winnings;
	
	
	public PhotoLeaderboardResponse() {}


	public Integer getRank() {
		return rank;
	}


	public void setRank(Integer rank) {
		this.rank = rank;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getFbID() {
		return fbID;
	}


	public void setFbID(String fbID) {
		this.fbID = fbID;
	}


	public Integer getPhoto_id() {
		return photo_id;
	}


	public void setPhoto_id(Integer photo_id) {
		this.photo_id = photo_id;
	}


	public Integer getLikes() {
		return likes;
	}


	public void setLikes(Integer likes) {
		this.likes = likes;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getCaption() {
		return caption;
	}


	public void setCaption(String caption) {
		this.caption = caption;
	}


	public Integer getWinnings() {
		return winnings;
	}


	public void setWinnings(Integer winnings) {
		this.winnings = winnings;
	}
	
	
	
}
