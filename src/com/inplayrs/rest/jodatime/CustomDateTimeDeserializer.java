package com.inplayrs.rest.jodatime;

import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


public class CustomDateTimeDeserializer extends JsonDeserializer<LocalDateTime>{
	
	private static DateTimeFormatter formatter = 
	        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
 	@Override
    public LocalDateTime deserialize(JsonParser par, DeserializationContext dsc)
        throws IOException, JsonProcessingException {

 		LocalDateTime dt = LocalDateTime.parse(par.getText().trim(), formatter);
 		
        return dt;
    }

}
