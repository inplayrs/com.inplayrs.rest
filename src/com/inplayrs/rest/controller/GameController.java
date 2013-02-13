package com.inplayrs.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.service.PeriodService;

import java.util.List;

import javax.annotation.Resource;
 

import org.springframework.web.bind.annotation.RequestParam;



//GET an array of Periods from /football/premier_league/week32/round/periods.json and map it into an object
//JSON looks like {"periods": [{"gameID": "2", "name": "Arsenal v Chelsea", "state": 0, "time": "(14')", "score": "1-0", "homePoints": "10", "drawPoints": "30", "awayPoints": "60", "selection": -1, "result": -1}]}
//POST an array of Selections to /football/premier_league/week32/round/selections.json
//JSON looks like {"selections": [{"gameID": "2", "selection": 0"}]}

/*
 * Handles game data requests
 */
@Controller
@RequestMapping("/game")
public class GameController {

	@Autowired
	@Resource(name="periodService")
	private PeriodService periodService;
	
	
	@RequestMapping(value = "/periods/{game_id}", method = RequestMethod.GET, headers="Accept=application/json")
    public @ResponseBody List<Period> getPeriodsForGame(@PathVariable int game_id) {
    	
		// Retrieve all persons by delegating the call to PersonService
		List<Period> periods = periodService.getPeriodsForGame(game_id);
		 
		return periods;
    }
	
	
	
	//@RequestMapping(value = "/period/entry/{game_id}", method = RequestMethod.POST, headers="Accept=application/json")
	
	
	
	
	
	
	
	
}
