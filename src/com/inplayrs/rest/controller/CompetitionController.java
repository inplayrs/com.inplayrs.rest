package com.inplayrs.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	@RequestMapping(value = "/fangroups", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<FanGroup> getFanGroupsInCompetition(@RequestParam(value="comp_id", required=true) Integer comp_id) {
    	
		List<FanGroup> fanGroups = competitionService.getFanGroupsInCompetition(comp_id);
		 
		return fanGroups;
    }
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<Competition> getCompetitions(@RequestParam(value="state", required=false) Integer state) {
    	
		List<Competition> competitions;
		
		if (state != null) {
			competitions = competitionService.getCompetitions(state);
		} else {
			competitions = competitionService.getCompetitions();
		}
		 
		return competitions;
    }
	
	
	@RequestMapping(value = "/games", method = RequestMethod.GET, headers="Accept=application/json")
	@ResponseStatus( HttpStatus.OK )
    public @ResponseBody List<Game> getGamesInCompetition(@RequestParam(value="comp_id", required=true) Integer comp_id, 
    													  @RequestParam(value="state", required=false) Integer state) {
		List<Game> games;
		
		if (state != null) {
			games = competitionService.getGamesInCompetition(comp_id, state);
		} else {
			games = competitionService.getGamesInCompetition(comp_id);
		}
	
		return games;
    }
	
	
	
	
	
	
}
