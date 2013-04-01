package com.inplayrs.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.inplayrs.rest.ds.TestTable;
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
	
	
	@RequestMapping(value = "/periods", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<Period> getPeriodsInGame(@RequestParam(value="game_id", required=true) Integer game_id) {
    	
		List<Period> periods = gameService.getPeriodsInGame(game_id);
		
		return periods;
    }
	
	
	@RequestMapping(value = "/game_entry", method = RequestMethod.POST, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.CREATED )
	public @ResponseBody Integer addGameEntry(@RequestBody GameEntry gameEntry) {

		return gameService.addGameEntry(gameEntry);
		 	
	}
	
	
    @RequestMapping(value = "/period_selection", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseStatus( HttpStatus.CREATED )
    public @ResponseBody Integer addPeriodSelection(@RequestBody PeriodSelection periodSelection) {
   
		return gameService.addPeriodSelection(periodSelection); 
		 	
	}
    

	
	
}
