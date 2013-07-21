package com.inplayrs.rest.constants;

import java.util.HashMap;
import java.util.Map;

public class Operators {

	public static final Map<String, String> opMap = new HashMap<String, String>();
	
	static {
		opMap.put(null, "=");
		opMap.put("eq", "=");
		opMap.put("ne", "!=");
		opMap.put("lt", "<");
		opMap.put("gt", ">");
	}
	
}
