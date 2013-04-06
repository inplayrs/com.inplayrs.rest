package com.inplayrs.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;

import com.inplayrs.rest.ds.Competition;
import com.inplayrs.rest.ds.FanGroup;
import com.inplayrs.rest.ds.Game;
import com.inplayrs.rest.service.CompetitionService;
import com.inplayrs.rest.service.GameService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@RequestMapping(value = "/fangroups", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<FanGroup> getFanGroupsInCompetition(@RequestParam(value="comp_id", required=true) Integer comp_id) {
    	
		List<FanGroup> fanGroups = competitionService.getFanGroupsInCompetition(comp_id);
		 
		return fanGroups;
    }
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<Competition> getCompetitions(@RequestParam(value="state", required=false) Integer state,
    													   @RequestParam(value="stateOP", required=false) String stateOP) {
    	
		// Validate stateOP parameter
		Map<String, Integer> validStateOperators = new HashMap<String, Integer>();
		validStateOperators.put(null, 1);
		validStateOperators.put("eq", 1);
		validStateOperators.put("ne", 1);
		validStateOperators.put("lt", 1);
		validStateOperators.put("gt", 1);
		
		if (!validStateOperators.containsKey(stateOP)) {
			throw new RuntimeException("Invalid stateOP value passed");
		}
		
		List<Competition> competitions;
		competitions = competitionService.getCompetitions(state, stateOP);	 
		return competitions;
    }
	
	
	@RequestMapping(value = "/games", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
	@ExceptionHandler(RuntimeException.class)
    public @ResponseBody List<Game> getGames(@RequestParam(value="comp_id", required=false) Integer comp_id, 
    										 @RequestParam(value="state", required=false) Integer state,
    										 @RequestParam(value="stateOP", required=false) String stateOP){
		
		// Validate stateOP parameter
		Map<String, Integer> validStateOperators = new HashMap<String, Integer>();
		validStateOperators.put(null, 1);
		validStateOperators.put("eq", 1);
		validStateOperators.put("ne", 1);
		validStateOperators.put("lt", 1);
		validStateOperators.put("gt", 1);
		
		if (!validStateOperators.containsKey(stateOP)) {
			throw new RuntimeException("Invalid stateOP value passed");
		}
		
		// Get list of games
		List<Game> games;
		games = competitionService.getGames(comp_id, state, stateOP);
		return games;
    }
	
	
	
	
	
	
}
