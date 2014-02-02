package com.inplayrs.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.inplayrs.rest.constants.LeaderboardType;
import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.PeriodSelection;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.responseds.GameLeaderboardResponse;
import com.inplayrs.rest.responseds.GamePointsResponse;
import com.inplayrs.rest.responseds.GameWinnersResponse;
import com.inplayrs.rest.responseds.PeriodResponse;
import com.inplayrs.rest.service.GameService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestParam;



/*
 * Handles game data requests
 */
@Controller
@RequestMapping("/game")
public class GameController {

	@Autowired
	@Resource(name="gameService")
	private GameService gameService;
	
	
	/*
	 * GET game/periods
	 */
	@RequestMapping(value = "/periods", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<PeriodResponse> getPeriodsInGame(
    	   @RequestParam(value="game_id", required=true) Integer game_id) {
    	
		// Get periods
		List<Period> periods = gameService.getPeriodsInGame(game_id);
		
		// Convert Periods into PeriodResponse objects
		// (objects that are in the format the client needs - mainly for alpha)
		ArrayList<PeriodResponse> periodResponses = new ArrayList<PeriodResponse>();
		for (Period p : periods) {
			PeriodResponse pr = new PeriodResponse(p);
			periodResponses.add(pr);
		}
		
		return periodResponses;
    }
	
	
	/*
	 * GET game/points
	 */
	@RequestMapping(value = "/points", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody GamePointsResponse getGamePoints(
    	   @RequestParam(value="game_id", required=true) Integer game_id,
    	   @RequestParam(value="includeSelections", required=false) Boolean includeSelections,
    	   @RequestParam(value="username", required=false) String username) {
    		
		// Get points for logged in user if no username provided
		if (username == null) {
			username = SecurityContextHolder.getContext().getAuthentication().getName();
		}
		
			
		// Get game points
		GamePointsResponse gamePoints = gameService.getGamePoints(game_id, username, includeSelections);
		
		return gamePoints;
    }
	
	
	/*
	 * POST game/selections
	 */
	@RequestMapping(value = "/selections", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Integer addGamePeriodSelections(
		   @RequestParam(value="game_id", required=true) Integer game_id,														
		   @RequestBody PeriodSelection[] periodSelections) {

		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return gameService.addGamePeriodSelections(game_id, username, periodSelections);
		 	
	}
	
	
	/*
	 * POST game/period/bank
	 */
	@RequestMapping(value = "/period/bank", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody PeriodSelection bankPeriodPoints(
		   @RequestParam(value="period_id", required=true) Integer period_id) {

		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return gameService.bankPeriodPoints(period_id, username);
		 	
	}
	
	
	
	/*
	 * GET game/leaderboard
	 */
	@RequestMapping(value = "/leaderboard", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List <GameLeaderboardResponse> getLeaderboard(
    	   @RequestParam(value="game_id", required=true) Integer game_id,
    	   @RequestParam(value="type", required=true) String type,
    	   @RequestParam(value="pool_id", required=false) Integer pool_id) {
    	
		// Validate leaderboard type
		if (!type.equals(LeaderboardType.GLOBAL) && !type.equals(LeaderboardType.FANGROUP) 
				&& !type.equals(LeaderboardType.USER_IN_FANGROUP) && !type.equals(LeaderboardType.POOL)) {
			throw new InvalidParameterException(new RestError(3200, "Invalid leaderboard type specified"));
		}
		
		// Must specify pool_id if this is a pool leaderboard
		if (type.equals(LeaderboardType.POOL) && pool_id == null) {
			throw new InvalidParameterException(new RestError(3201, "Must specify pool_id if this is a pool leaderboard"));
		}
		
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return gameService.getLeaderboard(game_id, type, username, pool_id);
    }
	
	
	/*
	 * GET game/winners
	 */
	@RequestMapping(value = "/winners", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<GameWinnersResponse> getGameWinners(
    	   @RequestParam(value="game_id", required=false) Integer game_id){
		
		// Get list of game winners
		return gameService.getGameWinners(game_id);
    }
	
}
