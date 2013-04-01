package com.inplayrs.rest.service;


import com.inplayrs.rest.ds.GameEntry;
import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.PeriodSelection;
import com.inplayrs.rest.ds.TestTable;

import java.util.HashSet;
import java.util.List;
import javax.annotation.Resource;
//import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/*
 * Service for processing game data
 */
@Service("gameService")
@Transactional
public class GameService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	
	/**
	  * Retrieves a single period by the period_id
	  */
	public Period getPeriod(int period_id) {
	    // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Retrieve existing period first
		Period period = (Period) session.get(Period.class, period_id);
		   
		return period;
	 }
	
	
	/**
	  * Retrieves all periods for a given game
	  * 
	  * @return list of Periods
	  */
	@SuppressWarnings("unchecked")
	public List<Period> getPeriodsInGame(Integer game_id) {
	   
		// Retrieve session from Hibernate, create query (HQL) and return a list of Periods
		Session session = sessionFactory.getCurrentSession(); 
		Query query = session.createQuery("FROM Period p where p.game.game_id = ".concat(game_id.toString()));
		return  query.list();
	 }
	
	
	 /*
	  * Adds a game entry
	  */
	 public Integer addGameEntry(GameEntry gameEntry) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Save
		return (Integer)session.save(gameEntry);
	 }
	 
	
	/**
	  * Adds a new period entry
	  */
	public Integer addPeriodSelection(PeriodSelection periodSelection) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Save
		return (Integer)session.save(periodSelection);
	}
	 
	
	
}
