package com.inplayrs.rest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
		
	public List<Competition> getCompetitions(Integer state, String stateOP) {
		
		Map<String, String> operators = new HashMap<String, String>();
		operators.put(null, "=");
		operators.put("eq", "=");
		operators.put("ne", "!=");
		operators.put("lt", "<");
		operators.put("gt", ">");
		
		StringBuffer queryString = new StringBuffer("from Competition c");
		
		if (state != null) {
			queryString.append(" where c.state ");
			queryString.append(operators.get(stateOP));
			queryString.append(" ");
			queryString.append(state);
		}
		
		// Retrieve session from Hibernate, create query (HQL) and return a list of competitions
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryString.toString());
		return  query.list();
	}
	
	
	public List<Game> getGames(Integer comp_id, Integer state, String stateOP) {
		
		Map<String, String> operators = new HashMap<String, String>();
		operators.put(null, "=");
		operators.put("eq", "=");
		operators.put("ne", "!=");
		operators.put("lt", "<");
		operators.put("gt", ">");
		
		StringBuffer queryString = new StringBuffer("from Game g");
		boolean hasWhereClause = false;
	
		if (comp_id != null) {
			queryString.append(" where g.competition.comp_id = ");
			queryString.append(comp_id);
			hasWhereClause = true;
		}
		
		if (state != null) {
			if (!hasWhereClause) {
				queryString.append(" where g.state ");
			} else {
				queryString.append(" and g.state ");				
			}
			queryString.append(operators.get(stateOP));
			queryString.append(" ");
			queryString.append(state);
		}
		
		// Retrieve session from Hibernate, create query (HQL) and return a list of games
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryString.toString());
		return  query.list();
	}
	
	
	
}
