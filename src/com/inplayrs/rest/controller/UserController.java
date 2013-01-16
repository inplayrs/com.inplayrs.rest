package com.inplayrs.rest.controller;

import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inplayrs.rest.ds.User;


/*
 * Handles user data requests
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/{username}", method = RequestMethod.GET, headers="Accept=application/json")
    public @ResponseBody User getUser(@PathVariable String username) {
    	
        
        if (username.equals("chris")) { 
        	User u = new User(1, "chris", "chris@inplayrs.com");
        	return u;
        }
        else if (username.equals("anil")) {
        	User u = new User(2, "anil", "anil@inplayrs.com");
        	return u;
    	}
        else {
        	User u = new User(0, "null", "no_email");
    		return u;    	
        }
        
    }
    
    
    
}




