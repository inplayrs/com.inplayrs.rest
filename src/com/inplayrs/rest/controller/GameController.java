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

import com.inplayrs.rest.ds.GameEntry;
import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.PeriodSelection;
import com.inplayrs.rest.responseds.GamePointsResponse;
import com.inplayrs.rest.service.GameService;

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
    public @ResponseBody List<Period> getPeriodsInGame(@RequestParam(value="game_id", required=true) Integer game_id) {
    	
		// Get periods
		List<Period> periods = gameService.getPeriodsInGame(game_id);
		
		return periods;
    }
	
	
	/*
	 * GET game/points
	 */
	@RequestMapping(value = "/points", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody GamePointsResponse getGamePoints(@RequestParam(value="game_id", required=true) Integer game_id) {
    		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
			
		// Get game points
		GamePointsResponse gamePoints = gameService.getGamePoints(game_id, username);
		
		return gamePoints;
    }
	
	
	/*
	 * POST game/selections
	 */
	@RequestMapping(value = "/selections", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Integer addGamePeriodSelections(@RequestParam(value="game_id", required=true) Integer game_id,														
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
	public @ResponseBody Integer bankPeriodPoints(@RequestParam(value="period_id", required=true) Integer period_id) {

		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return gameService.bankPeriodPoints(period_id, username);
		 	
	}
	
	
}
