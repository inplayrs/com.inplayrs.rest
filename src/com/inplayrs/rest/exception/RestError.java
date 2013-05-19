package com.inplayrs.rest.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestError {

	@JsonProperty
	private int code;
	
	@JsonProperty
	private String message;
	
    public RestError(String message) {
    	this.message = message;
    }
    
	public RestError(int code, String message) {
		this.code = code;
    	this.message = message;
    }
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
	
	
}
