package com.gnimsys.osaas.eclipse.ext.osaasjdbc.model;

import java.util.HashMap;
import java.util.Map;

public class OSaaSJDBCConstants {
	public static final String PROP_SQL_TYPE_REGULAR =  "REGULAR";
	public static final String PROP_SQL_TYPE_SUBJECT =  "SUBJECT";
	public static final String PROP_SQL_TYPE_SUBJECT_PVO =  "SUBJECT_PVO";
	public static final Map<String, String> BY_LABEL = new HashMap<>() {{
		put("Regular sql based on Tables/Views","REGULAR");
		put("Logical sql based on Subject Area","LOGICAL_SUBJECT");
	}};

}
