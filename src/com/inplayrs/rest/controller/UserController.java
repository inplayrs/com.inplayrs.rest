package com.inplayrs.rest.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.inplayrs.rest.ds.FanGroup;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.responseds.FangroupResponse;
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
	
    
	/*
	 * GET user/account
	 */
	@RequestMapping(value = "/account", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody User getUser(
    	   @RequestParam(value="username", required=true) String username) {
    	
		User user = userService.getUser(username);
		 
		return user;
    }
	
		
	
	/*
	 * POST user/fan
	 */
	@RequestMapping(value = "/fan", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Integer setUserFan(
		   @RequestParam(value="comp_id", required=true) Integer comp_id,
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
	public @ResponseBody User registerUser(
		   @RequestParam(value="username", required=true) String username,
		   @RequestParam(value="password", required=true) String password,
		   @RequestParam(value="email", required=false) String email,
		   @RequestParam(value="timezone", required=false) String timezone,
		   @RequestParam(value="deviceID", required=false) String deviceID,
		   @RequestParam(value="pushActive", required=false) Boolean pushActive) {

		return userService.registerUser(username, password, email, timezone, deviceID, pushActive);
		 	
	}
	
	
	
	/*
	 * POST user/account/update
	 */
	@RequestMapping(value = "/account/update", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody User updateAccount(
		   @RequestParam(value="password", required=false) String password,
		   @RequestParam(value="email", required=false) String email,
		   @RequestParam(value="timezone", required=false) String timezone,
		   @RequestParam(value="deviceID", required=false) String deviceID,
		   @RequestParam(value="pushActive", required=false) Boolean pushActive) {

		if (password == null && email == null && timezone == null && deviceID == null && pushActive == null) {
			// No account details specified to update
			return null;
		}
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return userService.updateAccount(username, password, email, timezone, deviceID, pushActive);
		 	
	}
	
    
	/*
	 * GET user/fangroups
	 */
	@RequestMapping(value = "/fangroups", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List <FangroupResponse> getUserFangroups() {
    	
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		List <FanGroup> fangroups = userService.getUserFangroups(username);
		
		// Convert FanGroups into FangroupResponse objects
		// (objects that are in the format the client needs - mainly for alpha)
		ArrayList<FangroupResponse> fangroupResponses = new ArrayList<FangroupResponse>();
		for (FanGroup fg : fangroups) {
			FangroupResponse fgr = new FangroupResponse(fg);
			fangroupResponses.add(fgr);
		}
		 
		return fangroupResponses;
    }
    
    
	
	/*
	 * POST user/pat
	 */
	@RequestMapping(value = "/pat", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody User pat(
		   @RequestParam(value="user", required=true) String user,
		   @RequestParam(value="comp_id", required=false) Integer comp_id,
		   @RequestParam(value="game_id", required=false) Integer game_id) {

		// Must either specify comp_id or game_id
		if (comp_id == null && game_id == null) {
			throw new InvalidParameterException(new RestError(2500, "Must specify either comp_id or game_id when patting a user"));
		}
		
		// Get username of player
		String fromUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if (fromUser.equals(user)) {
			throw new InvalidParameterException(new RestError(2503, "You cannot pat yourself!"));
		}
		
		return userService.pat(fromUser, user, comp_id, game_id);
		 	
	}
	
	
	
}




