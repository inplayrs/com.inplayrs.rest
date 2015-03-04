package com.inplayrs.rest.responseds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PhotoKeyResponse {
	
	@JsonProperty
	private Integer photo_id;
	
	@JsonProperty
	private String photoKey;
	
	public PhotoKeyResponse() {}

	public PhotoKeyResponse(Integer photo_id, String photoKey) {
		this.photo_id = photo_id;
		this.photoKey = photoKey;
	}
	
	
	public Integer getPhoto_id() {
		return photo_id;
	}

	public void setPhoto_id(Integer photo_id) {
		this.photo_id = photo_id;
	}

	public String getPhotoKey() {
		return photoKey;
	}

	public void setPhotoKey(String photoKey) {
		this.photoKey = photoKey;
	}
	
	
}
