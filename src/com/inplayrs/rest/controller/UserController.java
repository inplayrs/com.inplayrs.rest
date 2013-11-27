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

import org.apache.log4j.Logger;


/*
 * Handles user data requests
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	@Resource(name="userService")
	private UserService userService;
	
	//get log4j handler
	private static final Logger log = Logger.getLogger("APILog");
	
    
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
		   @RequestParam(value="username", required=false) String username,
		   @RequestParam(value="password", required=true) String password,
		   @RequestParam(value="email", required=false) String email,
		   @RequestParam(value="timezone", required=false) String timezone,
		   @RequestParam(value="deviceID", required=false) String deviceID,
		   @RequestParam(value="pushActive", required=false) Boolean pushActive,
		   @RequestParam(value="gcID", required=false) String gcID,
		   @RequestParam(value="gcUsername", required=false) String gcUsername,
		   @RequestParam(value="fbID", required=false) String fbID,
		   @RequestParam(value="fbUsername", required=false) String fbUsername,
		   @RequestParam(value="fbEmail", required=false) String fbEmail,
		   @RequestParam(value="fbFullName", required=false) String fbFullName ) {

		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// Check if Game Center registration
		if (gcID != null) {
			if (fbID != null || username != null) {
				log.error(authed_user+" | Cannot specify fbID or username as well as gcID when registering");		
				throw new InvalidParameterException(new RestError(2303, 
						"Cannot specify fbID or username as well as gcID when registering"));
			}
			
			return userService.registerGCUser(gcID, gcUsername, password, email, timezone, deviceID, pushActive);
			
		}
		
		// Check if FB registration
		if (fbID != null) {
			// Must only contain FB parameters
			if (gcID != null || username != null) {
				log.error(authed_user+" | Cannot specify gcID or username as well as fbID when registering");		
				throw new InvalidParameterException(new RestError(2304, 
						"Cannot specify gcID or username as well as fbID when registering"));
			}
			
			// Check that either username or full name is present
			if (fbUsername == null && fbFullName == null) {
				log.error(authed_user+" | Must specify either fbUsername or fbFullName with FB login");		
				throw new InvalidParameterException(new RestError(2310, 
						"Must specify either fbUsername or fbFullName with FB login"));
			}
			
			return userService.registerFBUser(fbID, fbUsername, fbEmail, fbFullName, password, email, timezone, deviceID, pushActive);
		}
		
		
		// Must be username registration
		if (username == null) {
			log.error(authed_user+" | Must specify either username, gcID or fbID when registering");
			throw new InvalidParameterException(new RestError(2305, 
					"Must specify either username, gcID or fbID when registering"));
		}
		
		// Regular username & password registration
		return userService.registerUser(username, password, email, timezone, deviceID, pushActive,
										null, null, null, null, null, null);
	
		
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




