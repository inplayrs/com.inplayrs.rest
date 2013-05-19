package com.inplayrs.rest.exception;


@SuppressWarnings("serial")
public class InvalidStateException extends RuntimeException{

	private RestError restError;
		
	public InvalidStateException(RestError restError) {
		super(restError.getMessage());
		this.restError = restError; 
	}

	public RestError getRestError() {
		return restError;
	}

	public void setRestError(RestError restError) {
		this.restError = restError;
	}

	
}
