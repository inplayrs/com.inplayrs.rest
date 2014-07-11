package com.inplayrs.rest.jodatime;


import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.inplayrs.rest.controller.TestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@SuppressWarnings("serial")
public class CustomObjectMapper extends ObjectMapper {

	//get log4j handler
	private static final Logger log = LogManager.getLogger(TestController.class);
	
    public CustomObjectMapper() {
    	
        super();
    	log.debug("Creating new CustomObjectMapper");
        registerModule(new JodaModule());
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

}
