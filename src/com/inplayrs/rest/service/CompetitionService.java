package com.inplayrs.rest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.inplayrs.rest.responseds.CompetitionLeaderboardResponse;
import com.inplayrs.rest.responseds.CompetitionPointsResponse;
import com.inplayrs.rest.responseds.CompetitionResponse;
import com.inplayrs.rest.responseds.GameResponse;


/*
 * Service for processing game data
 */
@Service("competitionService")
@Transactional
public class CompetitionService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	
	/*
	 * GET competition/fangroups
	 */
	@SuppressWarnings("unchecked")
	public List<FanGroup> getFanGroupsInCompetition(Integer comp_id) {
		// Retrieve session from Hibernate, create query (HQL) and return a list of fangroups
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM FanGroup f where f.competition.comp_id = ".concat(comp_id.toString()));
		return  query.list();
	}
	
		
	/*
	 * GET competition/list
	 */
	public List<CompetitionResponse> getCompetitions(Integer state, String stateOP, String username) {
		
		Map<String, String> operators = new HashMap<String, String>();
		operators.put(null, "=");
		operators.put("eq", "=");
		operators.put("ne", "!=");
		operators.put("lt", "<");
		operators.put("gt", ">");
		
		StringBuffer queryString = new StringBuffer("select c, max(ge.game_entry_id) from Competition c ");
		queryString.append("left join c.games g ");
		queryString.append("left join g.gameEntries ge ");
		queryString.append("with ge.user.username = '").append(username).append("'");
		
		
		if (state != null) {
			queryString.append(" where c.state ");
			queryString.append(operators.get(stateOP));
			queryString.append(" ");
			queryString.append(state);
		}
		
		queryString.append(" group by c.comp_id");
		
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
	public List<GameResponse> getGames(Integer comp_id, Integer state, String stateOP, String username) {
		
		Map<String, String> operators = new HashMap<String, String>();
		operators.put(null, "=");
		operators.put("eq", "=");
		operators.put("ne", "!=");
		operators.put("lt", "<");
		operators.put("gt", ">");
		
		StringBuffer queryString = new StringBuffer("select ");
		queryString.append("g.game_id, ");
		queryString.append("g.name, ");
		queryString.append("c.category as category_id, ");
		queryString.append("g.competition as competition_id, ");
		queryString.append("g.game_type, ");
		queryString.append("g.start_date, ");
		queryString.append("g.state, ");
		queryString.append("g.stake, ");
		queryString.append("g.num_players, ");
		queryString.append("g.global_pot_size, ");
		queryString.append("g.fangroup_pot_size, ");
		queryString.append("(CASE  ");
		queryString.append("WHEN ge.game_entry_id is NULL THEN false ");
		queryString.append("ELSE true ");
		queryString.append("END) as entered, ");
		queryString.append("g.banner_position, ");
		queryString.append("g.banner_image_url ");
		queryString.append("from ");
		queryString.append("game g ");
		queryString.append("left join competition c on c.comp_id = g.competition ");
		queryString.append("left join game_entry ge on (ge.game = g.game_id and ge.user = '");
		queryString.append(username).append("') ");
		
		
		boolean hasWhereClause = false;
	
		if (comp_id != null) {
			queryString.append("where g.competition = ");
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
		
		SQLQuery query = session.createSQLQuery(queryString.toString());
		query.addScalar("game_id");
		query.addScalar("name");
		query.addScalar("category_id");
		query.addScalar("competition_id");
		query.addScalar("game_type");
		query.addScalar("start_date");
		query.addScalar("state");
		query.addScalar("stake");
		query.addScalar("num_players");
		query.addScalar("global_pot_size");
		query.addScalar("fangroup_pot_size");
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
				StringBuffer fanQueryString = new StringBuffer("from Fan f where f.fangroup.competition = ");
				fanQueryString.append(comp_id);
				fanQueryString.append(" and f.user = '");
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
		queryString.append("left join fangroup_comp_leaderboard fcl on  ");
		queryString.append("(fcl.competition = gcl.competition and fcl.fangroup_id = gcl.fangroup_id) ");
		queryString.append("left join user_in_fangroup_comp_leaderboard uifcl on  ");
		queryString.append("(uifcl.competition = gcl.competition and uifcl.user = gcl.user) ");
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("g.competition, ");
		queryString.append("count(distinct ge.user) as global_pool_size ");
		queryString.append("from ");
		queryString.append("game_entry ge ");
		queryString.append("left join game g on g.game_id = ge.game ");
		queryString.append("where ");
		queryString.append("g.competition = ").append(comp_id).append(" and ");
		queryString.append("ge.user != 'Monkey' ");
		queryString.append(") as global_pool on global_pool.competition = gcl.competition ");
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("g.competition, ");
		queryString.append("count(distinct fg.name) as num_fangroups_entered ");
		queryString.append("from ");
		queryString.append("game_entry ge ");
		queryString.append("left join game g on g.game_id = ge.game ");
		queryString.append("left join fan f on f.user = ge.user ");
		queryString.append("left join fangroup fg on fg.fangroup_id = f.fangroup ");
		queryString.append("where ");
		queryString.append("g.competition = ").append(comp_id);
		queryString.append(" and fg.competition = ").append(comp_id);
		queryString.append(" and f.user != 'Monkey' ");
		queryString.append(") as num_fangroups on num_fangroups.competition = gcl.competition ");
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("g.competition, ");
		queryString.append("fg.fangroup_id, ");
		queryString.append("count(distinct ge.user) as fangroup_pool_size ");
		queryString.append("from ");
		queryString.append("game_entry ge ");
		queryString.append("left join game g on g.game_id = ge.game ");
		queryString.append("left join fan f on f.user = ge.user ");
		queryString.append("left join fangroup fg on fg.fangroup_id = f.fangroup ");
		queryString.append("where ");
		queryString.append("g.competition = ").append(comp_id);
		queryString.append(" and fg.competition = ").append(comp_id);
		queryString.append(" and f.user != 'Monkey'  ");
		queryString.append("group by fg.fangroup_id ");
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
		queryString.append("gcl.user = '").append(username).append("' ");
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
			return null;
		} else {	
			CompetitionPointsResponse cpr = (CompetitionPointsResponse) result.get(0);		
			return cpr;
		}

		
	}
	
	
	
}
