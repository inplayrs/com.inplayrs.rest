package com.inplayrs.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;

import com.inplayrs.rest.constants.LeaderboardType;
import com.inplayrs.rest.ds.FanGroup;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.responseds.CompetitionPointsResponse;
import com.inplayrs.rest.responseds.CompetitionResponse;
import com.inplayrs.rest.responseds.CompetitionWinnersResponse;
import com.inplayrs.rest.responseds.GameResponse;
import com.inplayrs.rest.responseds.CompetitionLeaderboardResponse;
import com.inplayrs.rest.service.CompetitionService;

import java.util.List;

import javax.annotation.Resource;

/*
 * Handles competition & event data requests
 */
@Controller
@RequestMapping("/competition")
public class CompetitionController {

	@Autowired
	@Resource(name="competitionService")
	private CompetitionService competitionService;
	
	/*
	 * GET competition/fangroups
	 */
	@RequestMapping(value = "/fangroups", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<FanGroup> getFanGroupsInCompetition(
    	   @RequestParam(value="comp_id", required=true) Integer comp_id) {
    	
		List<FanGroup> fanGroups = competitionService.getFanGroupsInCompetition(comp_id);
		 
		return fanGroups;
    }
	
	
	/*
	 * GET competition/list
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<CompetitionResponse> getCompetitions(
    	   @RequestParam(value="state", required=false) Integer state,
    	   @RequestParam(value="stateOP", required=false) String stateOP) {
    	
		
		/*
		 * TODO: Deco state and stateOP parameters, as the logic is now in the API
		 */
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		  
		return competitionService.getCompetitions(username);	
    }
	
	
	/*
	 * GET competition/games
	 */
	@RequestMapping(value = "/games", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<GameResponse> getGames(
    	   @RequestParam(value="comp_id", required=true) Integer comp_id, 
    	   @RequestParam(value="state", required=false) Integer state,
    	   @RequestParam(value="stateOP", required=false) String stateOP){
		
		/*
		 * TODO: Deco state and stateOP parameters, as the logic is now in the API
		 */
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// Get list of games
		return competitionService.getGames(comp_id, username);
    }
	
	
	/*
	 * GET competition/leaderboard
	 */
	@RequestMapping(value = "/leaderboard", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List <CompetitionLeaderboardResponse> getLeaderboard(
    	   @RequestParam(value="comp_id", required=true) Integer comp_id,
    	   @RequestParam(value="type", required=true) String type,
    	   @RequestParam(value="pool_id", required=false) Integer pool_id) {
    	
		// Validate leaderboard type
		if (!type.equals(LeaderboardType.GLOBAL) && !type.equals(LeaderboardType.FANGROUP) 
				&& !type.equals(LeaderboardType.USER_IN_FANGROUP) && !type.equals(LeaderboardType.POOL)) {
			throw new InvalidParameterException(new RestError(3300, "Invalid leaderboard type specified"));
		}
		
		// Must specify pool_id if this is a pool leaderboard
		if (type.equals(LeaderboardType.POOL) && pool_id == null) {
			throw new InvalidParameterException(new RestError(3301, "Must specify pool_id if this is a pool leaderboard"));
		}
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return competitionService.getLeaderboard(comp_id, type, username, pool_id);
    }
	

	/*
	 * GET competition/points
	 */
	@RequestMapping(value = "/points", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody CompetitionPointsResponse getCompetitionPoints(
    	   @RequestParam(value="comp_id", required=true) Integer comp_id) {
    		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
			
		// Get game points
		CompetitionPointsResponse competitionPoints = competitionService.getCompetitionPoints(comp_id, username);
		
		return competitionPoints;
    }
	
	
	
	/*
	 * GET competition/winners
	 */
	@RequestMapping(value = "/winners", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<CompetitionWinnersResponse> getCompetitionWinners(
    	   @RequestParam(value="comp_id", required=false) Integer comp_id){
		
		// Get list of competition winners
		return competitionService.getCompetitionWinners(comp_id);
    }
	
	
}
