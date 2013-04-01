package com.inplayrs.rest.service;

import java.util.List;
import javax.annotation.Resource;
//import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.ds.Competition;
import com.inplayrs.rest.ds.FanGroup;
import com.inplayrs.rest.ds.Game;


/*
 * Service for processing game data
 */
@Service("competitionService")
@Transactional
public class CompetitionService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	
	public List<FanGroup> getFanGroupsInCompetition(Integer comp_id) {
		// Retrieve session from Hibernate, create query (HQL) and return a list of fangroups
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM FanGroup f where f.competition.comp_id = ".concat(comp_id.toString()));
		return  query.list();
	}
	
	
	public List<Competition> getCompetitions(Integer state) {   
		// Retrieve session from Hibernate, create query (HQL) and return a list of competitions
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Competition c where c.state = ".concat(state.toString()));
		return  query.list();
	 }
	
	public List<Competition> getCompetitions() {   
		// Retrieve session from Hibernate, create query (HQL) and return a list of competitions
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Competition c");
		return  query.list();
	}
	
	
	public List<Game> getGamesInCompetition(Integer comp_id, Integer state) {
		// Retrieve session from Hibernate, create query (HQL) and return a list of games
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Game g where g.competition.comp_id = ".concat(comp_id.toString()).concat(" and g.state = ").concat(state.toString()));
		return  query.list();
	}
	
	public List<Game> getGamesInCompetition(Integer comp_id) {
		// Retrieve session from Hibernate, create query (HQL) and return a list of games
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Game g where g.competition.comp_id = ".concat(comp_id.toString()));
		return  query.list();
	}
	
}
