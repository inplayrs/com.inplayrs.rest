package com.inplayrs.rest.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.inplayrs.rest.service.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;


public class RestAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	@Resource(name="userService")
	private UserService userService;
	
	
	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RestToken restToken = (RestToken) authentication;

        String key = restToken.getKey();
        String credentials = restToken.getCredentials();

        // Verify the username and password
        if (!userService.authenticate(key, credentials)) {
            throw new BadCredentialsException("Invalid username / password");
        }

        return getAuthenticatedUser(key, credentials);
    }

    private Authentication getAuthenticatedUser(String key, String credentials) {
    	// Give every user the ROLE_USER role for now.  In future we could query DB
    	// to find out what roles a user has (although with our REST API all users
    	// will have the same role(s)
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new RestToken(key, credentials, authorities);
    }

    @Override
    /*
        Determines if this class can support the token provided by the filter.
     */
    public boolean supports(Class<?> authentication) {
        return RestToken.class.equals(authentication);
    }
	
	
}
