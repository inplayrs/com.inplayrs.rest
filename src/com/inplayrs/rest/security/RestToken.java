package com.inplayrs.rest.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@SuppressWarnings("serial")
public class RestToken extends UsernamePasswordAuthenticationToken {


	public RestToken(String key, String credentials) {
	    super(key, credentials);
	}
	
	public RestToken(String key, String credentials, Collection<? extends GrantedAuthority> authorities) {
	    super(key, credentials, authorities);
	}
	
	public String getKey() {
	    return (String) super.getPrincipal();
	}
	
	public String getCredentials() {
	    return (String) super.getCredentials();
	}
	
	
	
}
