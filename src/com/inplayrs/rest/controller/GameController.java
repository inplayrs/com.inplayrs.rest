package com.inplayrs.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.inplayrs.rest.ds.GameEntry;
import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.PeriodEntry;
import com.inplayrs.rest.ds.TestTable;
import com.inplayrs.rest.service.GameService;

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
	@Resource(name="gameService")
	private GameService gameService;
	
	
	@RequestMapping(value = "/periods", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<Period> getPeriodsForGame(@RequestParam(value="game_id", required=true) int game_id) {
    	
		List<Period> periods = gameService.getPeriodsForGame(game_id);
		 
		return periods;
    }
	
	
	@RequestMapping(value = "/game_entry", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Integer addGameEntry(@RequestBody GameEntry gameEntry) {

		return gameService.addGameEntry(gameEntry);
		 	
	}
	
	
    @RequestMapping(value = "/period_entry", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseStatus( HttpStatus.CREATED )
    public @ResponseBody Integer addPeriodEntry(@RequestBody PeriodEntry periodEntry) {
   
		return gameService.addPeriodEntry(periodEntry); 
		 	
	}
    
    
    @RequestMapping(value = "/test_table", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseStatus( HttpStatus.CREATED )
    public @ResponseBody Integer addTestTable(@RequestBody TestTable testTable) {
   
    	System.out.println("Entering test table POST");
		return gameService.addTestTable(testTable); 
		 	
	}
	    
    
	@RequestMapping(value = "/test_tables", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<TestTable> getTestTables() {
    	
		System.out.println("Entering GET test table list");
		List<TestTable> testTables = gameService.getTestTables();
		 
		return testTables;
    }
	
	
	@RequestMapping(value = "/test_table", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody TestTable getTestTable(@RequestParam(value="id", required=true) int id) {
    	
		System.out.println("Entering GET test table by ID");
		TestTable testTable = gameService.getTestTable(id);
		 
		return testTable;
    }
	
	
}
