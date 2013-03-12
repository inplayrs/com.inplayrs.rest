package com.inplayrs.rest.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.service.GameService;
import com.inplayrs.rest.service.UserService;


/*
 * Handles user data requests
 */
@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	@Resource(name="userService")
	private UserService userService;
	
    
	@RequestMapping(value = "/lookup", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody User getUser(@RequestParam(value="user_id", required=true) int user_id) {
    	
		User user = userService.getUser(user_id);
		 
		return user;
    }
	
    
    
    
    
}




