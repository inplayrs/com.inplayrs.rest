package com.inplayrs.rest.responseds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PhotoResponse {

	@JsonProperty
	private int photo_id;
	
	@JsonProperty
	private int user_id;
	
	@JsonProperty	
	private String url;
	
	@JsonProperty	
	private String caption;

	@JsonProperty	
	private int likes;
	
	public PhotoResponse() {}

	public int getPhoto_id() {
		return photo_id;
	}

	public void setPhoto_id(int photo_id) {
		this.photo_id = photo_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
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

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	
	
}
