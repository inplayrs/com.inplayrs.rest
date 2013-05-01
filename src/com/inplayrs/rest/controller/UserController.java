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
	
	
	/*
	 * POST user/register
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody User registerUser(@RequestParam(value="username", required=true) String username,
										   @RequestParam(value="password", required=true) String password,
										   @RequestParam(value="email", required=false) String email) {

		return userService.registerUser(username, password, email);
		 	
	}
	
	
	
	/*
	 * POST user/account/update
	 */
	@RequestMapping(value = "/account/update", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody User updateAccount(@RequestParam(value="password", required=false) String password,
										    @RequestParam(value="email", required=false) String email) {

		if (password == null && email == null) {
			// No account details specified to update
			return null;
		}
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return userService.updateAccount(username, password, email);
		 	
	}
	
    
    
    
    
}




