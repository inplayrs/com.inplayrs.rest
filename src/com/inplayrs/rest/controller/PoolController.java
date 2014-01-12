package com.inplayrs.rest.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.inplayrs.rest.ds.Pool;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.service.PoolService;

/*
 * Handles user data requests
 */
@Controller
@RequestMapping("/pool")
public class PoolController {
	
	@Autowired
	@Resource(name="poolService")
	private PoolService poolService;

	//get log4j handler
	private static final Logger log = Logger.getLogger("APILog");
	
	/*
	 * POST pool/create
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Pool createPool(
		   @RequestParam(value="name", required=true) String name,
		   @RequestParam(value="userList", required=false) String userList) {

		return poolService.createPool(name, userList);	 	
	}
	
	
	/*
	 * POST pool/adduser
	 */
	@RequestMapping(value = "/adduser", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Boolean addPoolMember(
		   @RequestParam(value="pool_id", required=true) Integer pool_id,
		   @RequestParam(value="username", required=false) String username, 
		   @RequestParam(value="fbID", required=false) String fbID) {

		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();

		// Must specify either username or fbID of user to be added to pool
		if (username == null && fbID == null) {
			log.error(authed_user+" | Must specify username or fbID of user to be added to pool");
			throw new InvalidParameterException(new RestError(2900, "Must specify username or fbID of user to be added to pool"));
		}
		
		// Must specify either username or fbID of user to be added to pool
		if (username != null && fbID != null) {
			log.error(authed_user+" | Must specify username or fbID of user to be added to pool, not both");
			throw new InvalidParameterException(new RestError(2901, "Must specify username or fbID of user to be added to pool, not both"));
		}
		
		return poolService.addPoolMember(pool_id, username, fbID);	 	
	}
	
	
}
