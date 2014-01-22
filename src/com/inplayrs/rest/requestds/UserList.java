package com.inplayrs.rest.requestds;

import java.util.List;

public class UserList {

	public List<String> usernames;
	
	public List<String> facebookIDs;
	
	public UserList() {
		
	}

	public List<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}

	public List<String> getFacebookIDs() {
		return facebookIDs;
	}

	public void setFacebookIDs(List<String> facebookIDs) {
		this.facebookIDs = facebookIDs;
	}
	
	
	
}
