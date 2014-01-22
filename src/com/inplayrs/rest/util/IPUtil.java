package com.inplayrs.rest.util;

import java.util.List;

public class IPUtil {

	public static String listToCommaSeparatedString(List<String> stringList) {
		if (stringList != null && !stringList.isEmpty()) {
			StringBuffer commaSeparatedString = new StringBuffer();
			for (String s : stringList) {
				if (commaSeparatedString.length() > 0) {
					commaSeparatedString.append(", ").append(s);
				} else {
					commaSeparatedString.append(s);
				}
			}
			return commaSeparatedString.toString();
		} else {
			return null;
		}
	}
	
}
