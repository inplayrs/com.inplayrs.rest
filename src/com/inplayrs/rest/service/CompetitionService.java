package com.inplayrs.rest.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.joda.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.constants.State;
import com.inplayrs.rest.ds.Competition;
import com.inplayrs.rest.ds.Fan;
import com.inplayrs.rest.ds.FanGroup;
import com.inplayrs.rest.responseds.CompetitionLeaderboardResponse;
import com.inplayrs.rest.responseds.CompetitionPointsResponse;
import com.inplayrs.rest.responseds.CompetitionResponse;
import com.inplayrs.rest.responseds.CompetitionWinnersResponse;
import com.inplayrs.rest.responseds.GameResponse;


/*
 * Service for processing game data
 */
@Service("competitionService")
@Transactional
public class CompetitionService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	//get log4j handler
	private static final Logger log = Logger.getLogger("APILog");
	
	
	/*
	 * GET competition/fangroups
	 */
	@SuppressWarnings("unchecked")
	public List<FanGroup> getFanGroupsInCompetition(Integer comp_id) {
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(username+" | Getting fangroups for competition "+comp_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a list of fangroups
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM FanGroup f where f.competition.comp_id = ".concat(comp_id.toString()));
				
		return  query.list();
	}
	
		
	/*
	 * GET competition/list
	 */
	public List<CompetitionResponse> getCompetitions(String username) {
				
		log.debug(username+" | Getting list of competitions");
		
		StringBuffer queryString = new StringBuffer("select c, max(ge.game_entry_id) from Competition c ");
		queryString.append("left join c.games g ");
		queryString.append("left join g.gameEntries ge ");
		queryString.append("left join ge.user u ");
		queryString.append("with u.username = '").append(username).append("' ");

		// Filter competitions by state
		queryString.append("where c.state in (");
		queryString.append(State.PREPLAY).append(", ");
		queryString.append(State.TRANSITION).append(", ");
		queryString.append(State.INPLAY).append(", ");
		queryString.append(State.COMPLETE).append(", ");
		queryString.append(State.SUSPENDED).append(", ");
		queryString.append(State.NEVERINPLAY).append(") ");
		
		queryString.append("group by c.comp_id");
		
		// Retrieve session from Hibernate, create query (HQL) and return a list of competitions
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryString.toString());
		
		ArrayList<CompetitionResponse> response = new ArrayList<CompetitionResponse>();
					
		@SuppressWarnings("rawtypes")
		Iterator compAndEntry = query.list().iterator();
		while(compAndEntry.hasNext()) {
			Object[] tuple = (Object[]) compAndEntry.next();
			
			Competition comp = (Competition) tuple[0];
			Integer entry = (Integer) tuple[1];
			  
			CompetitionResponse cr = new CompetitionResponse(comp);
			if (entry != null) {
				cr.setEntered(true);
			} else {
				cr.setEntered(false);
			}
			
			response.add(cr);	 
		}
		
		return  response;
	}
	
	
	/*
	 * GET competition/games
	 */
	@SuppressWarnings("unchecked")
	public List<GameResponse> getGames(Integer comp_id, String username) {
		
		log.debug(username+" | Getting games for competition "+comp_id);
			
		StringBuffer queryString = new StringBuffer("select ");
		queryString.append("g.game_id, ");
		queryString.append("g.name, ");
		queryString.append("c.category as category_id, ");
		queryString.append("g.game_type, ");
		queryString.append("g.start_date, ");
		queryString.append("g.state, ");
		queryString.append("g.num_players, ");
		queryString.append("(CASE  ");
		queryString.append("WHEN game_entries.game_entry_id is NULL THEN false ");
		queryString.append("ELSE true ");
		queryString.append("END) as entered, ");
		queryString.append("g.banner_position, ");
		queryString.append("g.banner_image_url ");
		queryString.append("from ");
		queryString.append("game g ");
		queryString.append("left join competition c on c.comp_id = g.competition ");
		queryString.append("left join (select ge.game_entry_id, ge.game, u.username ");
		queryString.append("from game_entry ge left join user u on ge.user = u.user_id) as game_entries ");	
		queryString.append("on game_entries.game = g.game_id and game_entries.username = '");
		queryString.append(username).append("' ");
		
		// Filter games by state
		queryString.append("where g.state in (");
		queryString.append(State.PREPLAY).append(", ");
		queryString.append(State.TRANSITION).append(", ");
		queryString.append(State.INPLAY).append(", ");
		queryString.append(State.COMPLETE).append(", ");
		queryString.append(State.SUSPENDED).append(", ");
		queryString.append(State.NEVERINPLAY).append(") ");
		
		if (comp_id != null) {
			queryString.append(" and g.competition = ");
			queryString.append(comp_id);
		}
		
		
		// Retrieve session from Hibernate, create query (HQL) and return a list of games
		Session session = sessionFactory.getCurrentSession();
		
		SQLQuery query = session.createSQLQuery(queryString.toString());
		query.addScalar("game_id");
		query.addScalar("name");
		query.addScalar("category_id");
		query.addScalar("game_type");
		query.addScalar("start_date");
		query.addScalar("state");
		query.addScalar("num_players");
		query.addScalar("entered", org.hibernate.type.NumericBooleanType.INSTANCE); 
		query.addScalar("banner_position");
		query.addScalar("banner_image_url");
		query.setResultTransformer(Transformers.aliasToBean(GameResponse.class));
		
		return query.list();
		
	}


	/*
	 * GET competition/leaderboard
	 */
	@SuppressWarnings("unchecked")
	public List<CompetitionLeaderboardResponse> getLeaderboard(Integer comp_id, String type, String username) {

		log.debug(username+" | Getting "+type+" leaderboard for competition "+comp_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("select ");
		
		// Build query string based on type of leaderboard
		switch(type) {
			case "global":
				queryString.append("lb.rank, ");
				queryString.append("u.username as name, ");
				queryString.append("lb.games_played, ");
				queryString.append("lb.winnings ");
				queryString.append("from global_comp_leaderboard lb ");
				queryString.append("left join user u on u.user_id = lb.user ");
				queryString.append("where lb.competition = ");
				queryString.append(comp_id);
				break;
				
			case "fangroup":
				queryString.append("lb.rank, ");
				queryString.append("lb.fangroup_name as name, ");
				queryString.append("lb.games_played, ");
				queryString.append("lb.winnings ");
				queryString.append("from fangroup_comp_leaderboard lb ");
				queryString.append("where lb.competition = ");
				queryString.append(comp_id);
				break;
				
			case "userinfangroup":					
				queryString.append("lb.rank, ");
				queryString.append("u.username as name, ");
				queryString.append("lb.games_played, ");
				queryString.append("lb.winnings ");
				queryString.append("from user_in_fangroup_comp_leaderboard lb ");
				queryString.append("left join user u on u.user_id = lb.user ");
				queryString.append("where lb.competition = ");
				queryString.append(comp_id);
				queryString.append(" and lb.fangroup_id = ");
				
				// Get fangroup of  user
				StringBuffer fanQueryString = new StringBuffer("from Fan f where f.fangroup.competition = ");
				fanQueryString.append(comp_id);
				fanQueryString.append(" and f.user.username = '");
				fanQueryString.append(username);
				fanQueryString.append("'");
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

		// Only return top 100 users 
		queryString.append(" and lb.rank >= 1 ORDER BY lb.rank LIMIT 100");
		
		
		SQLQuery query = session.createSQLQuery(queryString.toString());
		query.addScalar("rank");
		query.addScalar("name");
		query.addScalar("games_played");
		query.addScalar("winnings");
		query.setResultTransformer(Transformers.aliasToBean(CompetitionLeaderboardResponse.class));
		
		return query.list();
	
	}


	/*
	 * GET competition/points
	 */
	public CompetitionPointsResponse getCompetitionPoints(Integer comp_id, String username) {

		log.debug(username+" | Getting users points for competition "+comp_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("select ");
		queryString.append("gcl.rank as global_rank, ");
		queryString.append("gcl.winnings as global_winnings, ");
		queryString.append("gcl.fangroup_name, ");
		queryString.append("fcl.rank as fangroup_rank, ");
		queryString.append("fcl.winnings as fangroup_winnings, ");
		queryString.append("uifcl.rank as user_in_fangroup_rank, ");	
		queryString.append("global_pool.global_pool_size, ");
		queryString.append("num_fangroups.num_fangroups_entered, ");
		queryString.append("fangroup_pool.fangroup_pool_size, ");
		queryString.append("global_winnings.total_global_winnings, ");
		queryString.append("fangroup_winnings.total_fangroup_winnings, ");
		queryString.append("userinfangroup_winnings.total_userinfangroup_winnings ");
		queryString.append("from  ");
		queryString.append("global_comp_leaderboard gcl ");
		queryString.append("left join user u on u.user_id = gcl.user ");
		queryString.append("left join fangroup_comp_leaderboard fcl on  ");
		queryString.append("(fcl.competition = gcl.competition and fcl.fangroup_id = gcl.fangroup_id) ");
		queryString.append("left join user_in_fangroup_comp_leaderboard uifcl on  ");
		queryString.append("(uifcl.competition = gcl.competition and uifcl.user = gcl.user) ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("gcl.competition, ");
		queryString.append("count(gcl.user) as global_pool_size ");
		queryString.append("from global_comp_leaderboard gcl ");
		queryString.append("where gcl.competition = ").append(comp_id);
		queryString.append(") as global_pool on global_pool.competition = gcl.competition ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("fcl.competition, ");
		queryString.append("count(distinct fcl.fangroup_id) as num_fangroups_entered ");
		queryString.append("from fangroup_comp_leaderboard fcl ");
		queryString.append("where fcl.competition = ").append(comp_id);
		queryString.append(") as num_fangroups on num_fangroups.competition = gcl.competition ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("uifcl.fangroup_id, ");
		queryString.append("count(uifcl.user) as fangroup_pool_size ");
		queryString.append("from user_in_fangroup_comp_leaderboard uifcl ");
		queryString.append("where uifcl.competition = ").append(comp_id);
		queryString.append(" group by uifcl.fangroup_id ");
		queryString.append(") as fangroup_pool on fangroup_pool.fangroup_id = gcl.fangroup_id ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("gcl.competition, ");
		queryString.append("sum(gcl.winnings) as total_global_winnings ");
		queryString.append("from  ");
		queryString.append("global_comp_leaderboard gcl ");
		queryString.append("where  ");
		queryString.append("gcl.competition = ").append(comp_id);
		queryString.append(" ) as global_winnings on global_winnings.competition = gcl.competition ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("fcl.competition, ");
		queryString.append("sum(fcl.winnings) as total_fangroup_winnings ");
		queryString.append("from  ");
		queryString.append("fangroup_comp_leaderboard fcl ");
		queryString.append("where  ");
		queryString.append("fcl.competition = ").append(comp_id);
		queryString.append(" ) as fangroup_winnings on fangroup_winnings.competition = gcl.competition ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("uifcl.competition, ");
		queryString.append("uifcl.fangroup_id, ");
		queryString.append("sum(uifcl.winnings) as total_userinfangroup_winnings ");
		queryString.append("from  ");
		queryString.append("user_in_fangroup_comp_leaderboard uifcl ");
		queryString.append("where  ");
		queryString.append("uifcl.competition = ").append(comp_id);
		queryString.append(" group by uifcl.fangroup_id ");
		queryString.append(") as userinfangroup_winnings on (userinfangroup_winnings.competition = gcl.competition and ");
		queryString.append("userinfangroup_winnings.fangroup_id = gcl.fangroup_id) ");
		
		queryString.append("where ");
		queryString.append("u.username = '").append(username).append("' ");
		queryString.append("and gcl.competition = ").append(comp_id);

		SQLQuery query = session.createSQLQuery(queryString.toString());
		
		query.addScalar("global_rank");
		query.addScalar("global_winnings");
		query.addScalar("fangroup_name");
		query.addScalar("fangroup_rank");
		query.addScalar("fangroup_winnings");
		query.addScalar("user_in_fangroup_rank");
		query.addScalar("global_pool_size", org.hibernate.type.IntegerType.INSTANCE);
		query.addScalar("num_fangroups_entered", org.hibernate.type.IntegerType.INSTANCE);
		query.addScalar("fangroup_pool_size", org.hibernate.type.IntegerType.INSTANCE);
		query.addScalar("total_global_winnings", org.hibernate.type.IntegerType.INSTANCE);
		query.addScalar("total_fangroup_winnings", org.hibernate.type.IntegerType.INSTANCE);
		query.addScalar("total_userinfangroup_winnings", org.hibernate.type.IntegerType.INSTANCE);
		
		query.setResultTransformer(Transformers.aliasToBean(CompetitionPointsResponse.class));

		@SuppressWarnings("unchecked")
		List<CompetitionPointsResponse> result = query.list();
		
		if (result.isEmpty()) {
			log.debug(username+" | No points found for this user in competition "+comp_id);
			return null;
		} else {	
			CompetitionPointsResponse cpr = (CompetitionPointsResponse) result.get(0);		
			return cpr;
		}

		
	}
	
	
	
	/*
	 * GET competition/winners
	 */
	public List<CompetitionWinnersResponse> getCompetitionWinners(Integer comp_id) {
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(username+" | Getting winners of competition "+comp_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a list of fangroups
		Session session = sessionFactory.getCurrentSession();
		
		// Get winners of competitions
		StringBuffer queryString = new StringBuffer("select ");
		queryString.append("gcl.competition.comp_id as comp_id, ");
		queryString.append("gcl.competition.name as competition, ");
		queryString.append("gcl.competition.category.cat_id, ");
		queryString.append("gcl.competition.end_date as comp_end_date, ");
		queryString.append("gcl.user.username as user from GlobalCompLeaderboard gcl where gcl.rank = 1");
		
		// Filter competitions by state
		queryString.append(" and gcl.competition.state in (");
		queryString.append(State.COMPLETE).append(", ");
		queryString.append(State.ARCHIVED).append(")");
		
		// Filter by competition ID if specified
		if (comp_id != null) {
			queryString.append(" and gcl.competition.comp_id = ");
			queryString.append(comp_id);
		}
			
		
		Query query = session.createQuery(queryString.toString());
			
		List<CompetitionWinnersResponse> response = new ArrayList<>();
		
		CompetitionWinnersResponse cwr = new CompetitionWinnersResponse();
		
		// Iterate over result set
		@SuppressWarnings("rawtypes")
		Iterator compWinners = query.list().iterator();
		while(compWinners.hasNext()) {
			// Process each competition winner and add to response object
			Object[] row = (Object[]) compWinners.next();
			Integer competition_id = (Integer) row[0];
			String competition = (String) row[1];
			Integer cat_id = (Integer) row[2];
			LocalDateTime compEndDate = (LocalDateTime) row[3];
			String user = (String) row[4];
		
			// If this winner is for the same competition that we're processing, 
			// add to the list of winners
			if (competition_id == cwr.getComp_id()) {
				List<String> winners = new ArrayList<String>();
				
				if (!cwr.getWinners().isEmpty()) {
					winners = cwr.getWinners();	
				}
				winners.add(user);
				cwr.setWinners(winners);
			} 
			else {
				// Add the previous winner response object to the result set
				if (cwr.getComp_id() != null) {
					response.add(cwr);
					cwr = new CompetitionWinnersResponse();
				}
				
				// Set the new winner response object
				cwr.setComp_id(competition_id);
				cwr.setCategory_id(cat_id);
				cwr.setCompetition(competition);
				cwr.setCompEndDate(compEndDate);
				
				List<String> winners = new ArrayList<String>();
				winners.add(user);
				cwr.setWinners(winners);
			}
		}
		// Add final result to response
		if (cwr.getComp_id() != null) {
			response.add(cwr);
		}
		
		return  response;
		
	}
	
	
	
}
