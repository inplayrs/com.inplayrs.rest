package com.inplayrs.rest.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.inplayrs.rest.ds.TestTable;
import com.inplayrs.rest.service.TestService;

import org.apache.log4j.Logger;

@Controller
@RequestMapping("/test")
public class TestController {
	
	//get log4j handler
	private static final Logger log = Logger.getLogger(TestController.class);

	@Autowired
	@Resource(name="testService")
	private TestService testService;
	
    @RequestMapping(value = "/create", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseStatus( HttpStatus.CREATED )
    public @ResponseBody Integer addTestTable(@RequestBody TestTable testTable) {
   
    	log.debug("Entering test table POST");
		return testService.addTestTable(testTable); 
		 	
	}
	    
    
	@RequestMapping(value = "/test_tables", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<TestTable> getTestTables() {
    	
		log.debug("Entering GET test table list");
		List<TestTable> testTables = testService.getTestTables();
		 
		return testTables;
    }
	
	
	@RequestMapping(value = "/test_table", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody TestTable getTestTable(@RequestParam(value="id", required=true) int id) {
    	
		System.out.println("Entering GET test table by ID");
		TestTable testTable = testService.getTestTable(id);
		 
		return testTable;
    }
	
	
}
