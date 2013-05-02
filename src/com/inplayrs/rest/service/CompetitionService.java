package com.inplayrs.rest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
//import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.ds.Competition;
import com.inplayrs.rest.ds.Fan;
import com.inplayrs.rest.ds.FanGroup;
import com.inplayrs.rest.ds.Game;
import com.inplayrs.rest.responseds.LeaderboardResponse;


/*
 * Service for processing game data
 */
@Service("competitionService")
@Transactional
public class CompetitionService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<FanGroup> getFanGroupsInCompetition(Integer comp_id) {
		// Retrieve session from Hibernate, create query (HQL) and return a list of fangroups
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM FanGroup f where f.competition.comp_id = ".concat(comp_id.toString()));
		return  query.list();
	}
	
		
	@SuppressWarnings("unchecked")
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
	
	
	@SuppressWarnings("unchecked")
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


	/*
	 * Returns a leaderboard
	 */
	@SuppressWarnings("unchecked")
	public List<LeaderboardResponse> getLeaderboard(Integer comp_id, String type, String username) {

		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("select ");
		
		// Build query string based on type of leaderboard
		switch(type) {
			case "global":
				queryString.append("gcl.rank, ");
				queryString.append("gcl.user as name, ");
				queryString.append("gcl.games_played, ");
				queryString.append("gcl.winnings ");
				queryString.append("from global_comp_leaderboard gcl ");
				queryString.append("where competition = ");
				queryString.append(comp_id);
				break;
				
			case "fangroup":
				queryString.append("fcl.rank, ");
				queryString.append("fcl.fangroup_name as name, ");
				queryString.append("fcl.games_played, ");
				queryString.append("fcl.winnings ");
				queryString.append("from fangroup_comp_leaderboard fcl ");
				queryString.append("where competition = ");
				queryString.append(comp_id);
				break;
				
			case "userinfangroup":					
				queryString.append("uifcl.rank, ");
				queryString.append("uifcl.user as name, ");
				queryString.append("uifcl.games_played, ");
				queryString.append("uifcl.winnings ");
				queryString.append("from user_in_fangroup_comp_leaderboard uifcl ");
				queryString.append("where competition = ");
				queryString.append(comp_id);
				queryString.append(" and fangroup_id = ");
				
				// Get fangroup of  user
				StringBuffer fanQueryString = new StringBuffer("from Fan f where f.FanGroup.competition = ");
				fanQueryString.append(comp_id);
				fanQueryString.append(" and f.user = ");
				fanQueryString.append(username);
				Query fanQuery = session.createQuery(fanQueryString.toString());
				
				List<Fan> result = fanQuery.list();
				if (result.isEmpty()) {
					return null;
				} else {
					Fan fan = result.get(0);
					queryString.append(fan.getFangroup_id());
				}
				
				break;
				
			default: return null;	
		}

		SQLQuery query = session.createSQLQuery(queryString.toString());
		query.addScalar("rank");
		query.addScalar("name");
		query.addScalar("games_played");
		query.addScalar("winnings");
		query.setResultTransformer(Transformers.aliasToBean(LeaderboardResponse.class));
		
		return query.list();
	
	}
	
	
	
}
