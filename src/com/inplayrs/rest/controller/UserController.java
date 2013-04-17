package com.inplayrs.rest.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.service.UserService;


/*
 * Handles user data requests
 */
@Controller
@RequestMapping("/user")
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
	
	
	
	/*
	 * POST user/fan
	 */
	@RequestMapping(value = "/fan", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Integer setUserFan(@RequestParam(value="comp_id", required=true) Integer comp_id,
											@RequestParam(value="fangroup_id", required=true) Integer fangroup_id) {

		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return userService.setUserFan(comp_id, fangroup_id, username);
		 	
	}
	
    
    
    
    
}




