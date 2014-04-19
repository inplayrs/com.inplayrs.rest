package com.inplayrs.rest.requestds;

import java.util.ArrayList;
import java.util.List;

import com.inplayrs.rest.util.IPUtil;

public class UserList {

	public List<String> usernames;
	
	public List<String> facebookIDs;
	
	public UserList() {
		usernames = new ArrayList<String>();
		facebookIDs = new ArrayList<String>();
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
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("usernames=");
		str.append(IPUtil.listToCommaSeparatedString(usernames));
		str.append(". facebookIDs=");
		str.append(IPUtil.listToCommaSeparatedString(facebookIDs));
		return str.toString();
	}
	
}
