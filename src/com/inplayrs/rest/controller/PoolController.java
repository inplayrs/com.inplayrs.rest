package com.inplayrs.rest.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.inplayrs.rest.ds.Pool;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.requestds.UserList;
import com.inplayrs.rest.responseds.MyPoolResponse;
import com.inplayrs.rest.responseds.PoolMemberResponse;
import com.inplayrs.rest.responseds.PoolPointsResponse;
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
	private static final Logger log = LogManager.getLogger(PoolController.class.getName());
	
	/*
	 * POST pool/create
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Pool createPool(
		   @RequestParam(value="name", required=true) String name,
		   @RequestBody(required = false) UserList userList) {

		return poolService.createPool(name, userList);	 	
	}
	
	
	/*
	 * POST pool/addusers
	 */
	@RequestMapping(value = "/addusers", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Boolean addPoolMember(
		   @RequestParam(value="pool_id", required=true) Integer pool_id,
		   @RequestBody(required = true) UserList userList) {

		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// UserList must contain at least 1 user
		if (userList.getUsernames().isEmpty() && userList.getFacebookIDs().isEmpty()) {
			log.error(authed_user+" | Must specify at least 1 user to be added to pool");
			throw new InvalidParameterException(new RestError(2900, "Must specify at least 1 user to be added to pool"));
		}
		
		return poolService.addPoolMembers(pool_id, userList);	 	
	}
	
	
	/*
	 * GET pool/mypools
	 */
	@RequestMapping(value = "/mypools", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
	public @ResponseBody List<MyPoolResponse> getMyPools() {
		
		return poolService.getMyPools();
	}
	
	
	
	/*
	 * GET pool/members
	 */
	@RequestMapping(value = "/members", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
	public @ResponseBody List<PoolMemberResponse> getMyPools(
			@RequestParam(value="pool_id", required=true) Integer pool_id) {
		
		return poolService.getPoolMembers(pool_id);
	}
	
	
	/*
	 * POST pool/leave
	 */
	@RequestMapping(value = "/leave", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
	public @ResponseBody Boolean leavePool(
			@RequestParam(value="pool_id", required=true) Integer pool_id){
		
		return poolService.leavePool(pool_id);
	}
	
	
	/*
	 * POST pool/update
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
	public @ResponseBody Pool updatePool(
			@RequestParam(value="pool_id", required=true) Integer pool_id,
			@RequestParam(value="name", required=true) String name){
		
		return poolService.updatePool(pool_id, name);
	}
	
	
	/*
	 * GET pool/points
	 */
	@RequestMapping(value = "/points", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
	public @ResponseBody PoolPointsResponse getPoolPoints(
			@RequestParam(value="pool_id", required=true) Integer pool_id,
			@RequestParam(value="game_id", required=false) Integer game_id,
			@RequestParam(value="comp_id", required=false) Integer comp_id){
		
		// Must specify either game_id or comp_id but not both
		if (game_id != null && comp_id != null) {
			throw new InvalidParameterException(new RestError(3400, "Must specify either game_id or comp_id, not both"));
		}
		if (game_id == null && comp_id == null) {
			throw new InvalidParameterException(new RestError(3401, "Must specify either game_id or comp_id"));
		}
		
		return poolService.getPoolPoints(pool_id, game_id, comp_id);
	}
}
