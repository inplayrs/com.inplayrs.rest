package com.inplayrs.rest.service;


import com.inplayrs.rest.ds.GameEntry;
import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.PeriodEntry;

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
 * Service for processing Periods
 */
@Service("gameService")
@Transactional
public class GameService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	
	/**
	  * Retrieves a single period by the period_id
	  */
	public Period get(int period_id) {
	    // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Retrieve existing period first
		Period period = (Period) session.get(Period.class, period_id);
		   
		return period;
	 }
	
	
	/**
	  * Retrieves all periods for a given game
	  * 
	  * @return a list of Periods
	  */
	 public List<Period> getPeriodsForGame(int game_id) {
	   
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM Period p where p.game_id = ".concat(Integer.toString(game_id)));
		   
		// Retrieve all
		return  query.list();
		
	 }
	
	
	
	 public void addGameEntry(GameEntry gameEntry) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Save
		session.save(gameEntry);
	 }
	 
	 
	 
	
	/**
	  * Adds a new period entry
	  */
	public void addPeriodEntry(PeriodEntry periodEntry) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Save
		session.save(periodEntry);
	}
	 
	
	
}
