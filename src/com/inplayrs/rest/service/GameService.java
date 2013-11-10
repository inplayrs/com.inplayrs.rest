package com.inplayrs.rest.service;


import com.inplayrs.rest.constants.State;
import com.inplayrs.rest.ds.Fan;
import com.inplayrs.rest.ds.Game;
import com.inplayrs.rest.ds.GameEntry;
import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.PeriodSelection;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.exception.DBException;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.InvalidStateException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.responseds.GameLeaderboardResponse;
import com.inplayrs.rest.responseds.GamePointsResponse;

import java.util.List;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import org.springframework.security.core.context.SecurityContextHolder;
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
	
	//get log4j handler
	private static final Logger log = Logger.getLogger("APILog");
	
	
	/*
	  * Retrieves a single period by the period_id
	  */
	public Period getPeriod(int period_id) {
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(username+" | Getting period "+period_id);
		
	    // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Retrieve existing period first
		Period period = (Period) session.get(Period.class, period_id);
		   
		return period;
	 }
	
	
	/*
	 * GET game/periods
	 */
	@SuppressWarnings("unchecked")
	public List<Period> getPeriodsInGame(Integer game_id) {
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(username+" | Getting periods in game "+game_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a list of Periods
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("FROM Period p where p.game.game_id = ");
		queryString.append(game_id);
		
		// Filter periods by state
		queryString.append(" and p.state in (");
		queryString.append(State.PREPLAY).append(", ");
		queryString.append(State.TRANSITION).append(", ");
		queryString.append(State.INPLAY).append(", ");
		queryString.append(State.COMPLETE).append(", ");
		queryString.append(State.SUSPENDED).append(", ");
		queryString.append(State.NEVERINPLAY).append(") ");
		
		Query query = session.createQuery(queryString.toString());
		return  query.list();
	 }
	


	@SuppressWarnings("unchecked")
	public List<PeriodSelection> getPeriodSelections(Integer game_id, String username) {
		
		log.debug(username+" | Getting period selections for game "+game_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("from PeriodSelection ps where ps.gameEntry.game_id = ");
		queryString.append(game_id.toString());
		queryString.append(" and ps.gameEntry.user.username = '");
		queryString.append(username);
		queryString.append("'");
		
		Query query = session.createQuery(queryString.toString());
		
		return query.list();
	}
	
	
	/*
	 * GET game/points
	 */
	public GamePointsResponse getGamePoints(Integer game_id, String username, Boolean includeSelections) {
		
		log.debug(username+" | Getting points for game "+game_id);
		
		// Default includeSelections to true
		if (includeSelections == null) { 
			includeSelections = true;
		}
		
		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("select ");
		queryString.append("g.global_pot_size, ");
		queryString.append("g.fangroup_pot_size, ");
		queryString.append("h2h.h2h_pot_size, ");
		queryString.append("ge.global_winnings, ");
		queryString.append("ge.fangroup_winnings, ");
		queryString.append("ge.h2h_winnings, ");
		queryString.append("ge.total_winnings, ");
		queryString.append("h2h_user.username as h2h_user, ");
		queryString.append("h2h_ge.total_points as h2h_points, ");
		queryString.append("fangrp.name as fangroup_name, ");
		queryString.append("ge.total_points as points, ");
		queryString.append("ggl.rank as global_rank, ");
		queryString.append("fgl.rank as fangroup_rank, ");
		queryString.append("uifgl.rank as user_in_fangroup_rank, ");
		queryString.append("g.num_players as global_pool_size, ");
		queryString.append("num_fangroups.num_fangroups_entered, ");
		queryString.append("fangroup_pool.fangroup_pool_size, ");
		queryString.append("CASE ");
		queryString.append("WHEN ge.entry_state = -1 THEN false ");
		queryString.append("WHEN ge.entry_state = 0 THEN false ");
		queryString.append("WHEN ge.entry_state IS NULL THEN false ");
		queryString.append("ELSE true ");
		queryString.append("END 'late_entry' ");

		queryString.append("from  ");
		queryString.append("game_entry ge ");
		queryString.append("left join user u on u.user_id = ge.user ");
		queryString.append("left join game g on g.game_id = ge.game ");
		queryString.append("left join h2h_pool h2h on ( (h2h.game_entry_1 = ge.game_entry_id) or ");
		queryString.append("(h2h.game_entry_2 = ge.game_entry_id) ) ");
		queryString.append("left join global_game_leaderboard ggl on (ggl.game = ge.game and ggl.user = ge.user) ");
		queryString.append("left join fangroup_game_leaderboard fgl on (fgl.game = ge.game and fgl.fangroup_id = ggl.fangroup_id) ");
		queryString.append("left join user_in_fangroup_game_leaderboard uifgl on (uifgl.game = ge.game and uifgl.user = ge.user) ");
		queryString.append("left join game_entry h2h_ge ON (h2h_ge.game = ge.game and h2h_ge.user = ");
		queryString.append("(CASE WHEN h2h.game_entry_1 = ge.game_entry_id then h2h.user_2 ");
		queryString.append(" WHEN h2h.game_entry_2 = ge.game_entry_id then h2h.user_1 ELSE null END) ) ");
		queryString.append("left join user h2h_user ON h2h_user.user_id = h2h_ge.user ");
		
		queryString.append("left join (");
		queryString.append("select fg.name, fg.fangroup_id, fg.competition, f.user ");
		queryString.append("from fangroup fg ");
		queryString.append("left join fan f on f.fangroup = fg.fangroup_id ");
		queryString.append("left join user u on u.user_id = f.user ");
		queryString.append("where u.username = '").append(username).append("'");
		queryString.append(") as fangrp	on fangrp.competition = g.competition ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("fgl.game as game_id, ");
		queryString.append("count(distinct fgl.fangroup_id) as num_fangroups_entered ");
		queryString.append("from fangroup_game_leaderboard fgl ");
		queryString.append("where fgl.game = ").append(game_id);	
		queryString.append(") as num_fangroups on num_fangroups.game_id = g.game_id ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("uifgl.fangroup_id, ");
		queryString.append("count(uifgl.user) as fangroup_pool_size ");
		queryString.append("from user_in_fangroup_game_leaderboard uifgl ");
		queryString.append("where uifgl.game = ").append(game_id);
		queryString.append(" group by uifgl.fangroup_id");
		queryString.append(") as fangroup_pool on fangroup_pool.fangroup_id = fangrp.fangroup_id ");

		queryString.append("where ge.game = ").append(game_id.toString());
		queryString.append(" and u.username = '").append(username).append("'");
		
		SQLQuery query = session.createSQLQuery(queryString.toString());
		
		query.addScalar("global_pot_size");
		query.addScalar("fangroup_pot_size");
		query.addScalar("h2h_pot_size");
		query.addScalar("global_winnings");
		query.addScalar("fangroup_winnings");
		query.addScalar("h2h_winnings");
		query.addScalar("total_winnings");
		query.addScalar("h2h_user");
		query.addScalar("h2h_points");
		query.addScalar("fangroup_name");
		query.addScalar("points");
		query.addScalar("global_rank");
		query.addScalar("fangroup_rank");
		query.addScalar("user_in_fangroup_rank");
		query.addScalar("global_pool_size");
		query.addScalar("num_fangroups_entered", org.hibernate.type.IntegerType.INSTANCE);
		query.addScalar("fangroup_pool_size",  org.hibernate.type.IntegerType.INSTANCE);
		query.addScalar("late_entry", org.hibernate.type.BooleanType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(GamePointsResponse.class));
				
		@SuppressWarnings("unchecked")
		List<GamePointsResponse> result = query.list();
		
		if (result.isEmpty()) {
			log.debug(username+" | No points found for game "+game_id);
			return null;
		} else {	
			GamePointsResponse gpr = (GamePointsResponse) result.get(0);
			if (includeSelections) {
				gpr.setPeriodSelections(getPeriodSelections(game_id, username));		
			}
			return gpr;
		}
	}
	
	


	/*
	 *  POST game/selections
	 */
	@SuppressWarnings("unchecked")
	public Integer addGamePeriodSelections(Integer game_id, String username, PeriodSelection[] periodSelections) {
		
		log.debug(username+" | Posting selections for game "+game_id);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();		

		// GameEntry for the selections
		GameEntry gameEntry = new GameEntry();
		
		// Keep track of whether this is the user's initial submit or not
		boolean isInitialSubmit = false;
		
		// Create game entry if we don't already have one
		Query gameEntryQuery = session.createQuery("from GameEntry ge where ge.game = "+game_id.toString()+
										   " and ge.user.username = '"+username+"'");
		
		List<GameEntry> result = null;
		try {
			result = gameEntryQuery.list();
		}
		catch (Exception e) {
			log.error(username+" | Failed to find user's entries for game "+game_id);
			throw new DBException(new RestError(1001, "Failed to find user's game entries"));
		}
		
		if (result.isEmpty()) {
			
			log.debug(username+" | This is user's initial submit for game "+game_id+", creating a new game entry");
			
			isInitialSubmit = true;
			
			// Create a new GameEntry
			Game g;
			try {
				g = (Game) session.get(Game.class, game_id);	
			}
			catch (Exception e) {
				log.error(username+" | Failed to get game "+game_id);
				throw new DBException(new RestError(1001, "Failed to get game "+game_id));
			}
			
			if (g == null) {
				log.error(username+" | Game "+game_id+" does not exist");
				throw new InvalidParameterException(new RestError(2200, "Game "+game_id+" does not exist"));
			}
			
			// Check that user has a fangroup before creating a game entry
			StringBuffer fangroupQueryString = new StringBuffer("from Fan f where f.user.username = '");
			fangroupQueryString.append(username).append("' and f.fangroup.competition = ");
			fangroupQueryString.append(g.getCompetition_id());
			
			Query fangroupQuery = session.createQuery(fangroupQueryString.toString());
			List <Fan> fan = fangroupQuery.list();
			if (fan.isEmpty()) {
				log.error(username+" | Cannot post selections, no fangroup selected for competition "+g.getCompetition_id());
				throw new InvalidStateException(new RestError(2201, "Please select a fangroup for this competition before entering!"));
			}
			
			
			Query userQuery = session.createQuery("FROM User u WHERE u.username = '"+username+"'");
			userQuery.setCacheable(true);
			userQuery.setCacheRegion("user");
			User usr = (User) userQuery.uniqueResult();
			
			
			// Update game entry with game details and save
			gameEntry.setGame_id(game_id);
			gameEntry.setUser_id(usr.getUser_id());
			gameEntry.setGame(g);
			gameEntry.setEntry_state(g.getState());
			gameEntry.setUser(usr);
			
			session.save(gameEntry);
		
			// Increment num players, global pot size & fangroup pot size in game and update
			g.setNum_players(g.getNum_players()+1);
			
			// Only increase fangroup pot size if it's not a late entry
			if (g.getState() == State.PREPLAY  || g.getState() == State.TRANSITION) {
				log.debug(username+" | Increasing fangroup_pot_size for game "+game_id);
				g.setFangroup_pot_size(g.getFangroup_pot_size() + g.getStake());
			}
			
			// Always increase global pot size
			g.setGlobal_pot_size(g.getGlobal_pot_size() + g.getStake());
			
			session.update(g);	

		} else {
			// get the game_entry_id
			gameEntry = (GameEntry) result.get(0);
		}
		
		
		for (PeriodSelection ps : periodSelections) {
			// Set the GameEntry for the period selection
			ps.setGameEntry(gameEntry);
			ps.setGame_entry_id(gameEntry.getGame_entry_id());

			Period period = (Period) session.load(Period.class, ps.getPeriod_id());
			ps.setPeriod(period);
			
			// Do not allow user to perform their initial submit of selections if a period is suspended
			if (isInitialSubmit && period.getState() == State.SUSPENDED) {
				log.error(username+" | Cannot post initial selections, one or more events are currently suspended for game "+game_id);
				session.delete(gameEntry);
				throw new InvalidStateException(new RestError(2202, "One or more events are currently suspended, please try again later!"));
			}
				
			// Set potential_points for period_selection
			switch(ps.getSelection()) {
				case 0 : ps.setPotential_points(period.getPoints0());
						 break;
				case 1 : ps.setPotential_points(period.getPoints1());
						 break;
				case 2 : ps.setPotential_points(period.getPoints2());
						 break;
				default: break;
			}
			
			Query periodSelectionQuery = session.createQuery("from PeriodSelection ps where ps.gameEntry = "+
															 gameEntry.getGame_entry_id()+
															 " and ps.period = "+
															 ps.getPeriod_id());
			
			List <PeriodSelection> psqResult = periodSelectionQuery.list();
			
			if (psqResult.isEmpty()) {
				// Add the new period selection if state is preplay/transition
				int period_state = ps.getPeriod().getState();
				if (period_state == State.PREPLAY || period_state == State.TRANSITION) {
					try {
						session.save(ps);
					}
					catch (Exception e) {
						log.error(username+" | Failed to create period selection for period "+ps.getPeriod_id());
						throw new DBException(new RestError(1000, "Failed to create new period selection"));
					}
				} else {
					log.debug(username+" | Could not update period selection, period "+ps.getPeriod_id()+" is no longer in preplay/transition");
				}
			} else {
				// Update if pre-play/transition and not cashed out
				PeriodSelection currentSelection = psqResult.get(0);
				int period_state = currentSelection.getPeriod().getState();
				if (period_state == State.PREPLAY || period_state == State.TRANSITION) {
					if (currentSelection.isCashed_out()) {
						log.debug(username+" | User has already banked their points for period "+ps.getPeriod_id());
					} else {
						log.debug(username+" | Updating selection for period "+ps.getPeriod_id());
						currentSelection.setSelection(ps.getSelection());
						currentSelection.setPotential_points(ps.getPotential_points());
						try {
							session.update(currentSelection);
						}
						catch (Exception e) {
							log.error(username+" | Failed to update period selection for period "+ps.getPeriod_id());
							throw new DBException(new RestError(1000, "Failed to update period selection"));
						}
					}
					
				} else {
					log.debug(username+" | Could not update period selection, period "+ps.getPeriod_id()+" is no longer in preplay/transition");
				}
				
			}
						
		}
		
		return null;
	}


	
	/*
	 * POST game/period/bank
	 */
	public PeriodSelection bankPeriodPoints(Integer period_id, String username) {
		
		log.debug(username+" | Banking points for period "+period_id);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		StringBuffer queryString = new StringBuffer("from PeriodSelection ps where ps.period = ");
		queryString.append(period_id);
		queryString.append(" and ps.gameEntry.user.username = '");
		queryString.append(username);
		queryString.append("'");
		
		Query query = session.createQuery(queryString.toString());
		
		@SuppressWarnings("unchecked")
		List<PeriodSelection> result = query.list();
		
		if (result.isEmpty()) {		
			log.error(username+" | Cannot bank points as no selection yet made for period "+period_id);
			throw new InvalidStateException(new RestError(2000, "You have not made a selection for this event yet. Cannot bank points.")); 
		} else {
			PeriodSelection ps = result.get(0);
			// Can't bank points if already banked
			if (ps.isCashed_out()) {
				log.error(username+" | Cannot bank points for period "+period_id+", user has already banked");
				throw new InvalidStateException(new RestError(2001, "You have already banked your points for this event"));
			}
			
			Period period = ps.getPeriod();
			
			// Can only bank points if period is still in transition/inplay
			int period_state = period.getState();
			if (period_state == State.TRANSITION || period_state == State.INPLAY) {
				// Update points won
				switch(ps.getSelection()) {
					case 0 : ps.setAwarded_points(period.getPoints0());
							 break;
					case 1 : ps.setAwarded_points(period.getPoints1());
							 break;
					case 2 : ps.setAwarded_points(period.getPoints2());
							 break;
					default: break;
				}
				
				// Set banked
				ps.setCashed_out(true);
				
				// Update PeriodSelection
				session.update(ps);
					
				// Update total_points in game entry
				GameEntry ge = ps.getGameEntry();
				ge.setTotal_points(ge.getTotal_points() + ps.getAwarded_points());
				session.update(ge);
				
				// Return null for alpha
				return null;
				
			} else {	
				if (period_state == State.PREPLAY || period_state == State.TRANSITION) {
					log.error(username+" | Cannot bank points for period "+period_id+", event has not started");
					throw new InvalidStateException(new RestError(2002, "Cannot bank points, event has not started yet"));
				} else if (period_state == State.COMPLETE) {
					log.error(username+" | Cannot bank points for period "+period_id+", event has now completed");
					throw new InvalidStateException(new RestError(2003, "Cannot bank points, event has now completed"));
				} else if (period_state == State.SUSPENDED) {
					log.error(username+" | Cannot bank points for period "+period_id+", event is currently suspended");
					throw new InvalidStateException(new RestError(2004, "Cannot bank points, event is currently suspended. Please try again later!"));
				} else {
					log.error(username+" | Cannot bank points for period "+period_id+", unknown period_state: "+period_state);
					throw new InvalidStateException(new RestError(2005, "Cannot bank points, unknown period_state: "+period_state));
				}
			}

		}
			
	}


	/*
	 * GET game/leaderboard
	 */
	@SuppressWarnings("unchecked")
	public List<GameLeaderboardResponse> getLeaderboard(Integer game_id,
			String type, String username) {

		log.debug(username+" | Getting "+type+" leaderboard for game "+game_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("select ");
		
		// Build query string based on type of leaderboard
		switch(type) {
			case "global":
				queryString.append("lb.rank, ");
				queryString.append("u.username as name, ");
				queryString.append("lb.points, ");
				queryString.append("lb.potential_winnings ");
				queryString.append("from global_game_leaderboard lb ");
				queryString.append("left join user u on u.user_id = lb.user ");
				queryString.append("where lb.game = ");
				queryString.append(game_id);
				break;
				
			case "fangroup":
				queryString.append("lb.rank, ");
				queryString.append("lb.fangroup_name as name, ");
				queryString.append("lb.avg_points as points, ");
				queryString.append("lb.potential_winnings ");
				queryString.append("from fangroup_game_leaderboard lb ");
				queryString.append("where lb.game = ");
				queryString.append(game_id);
				break;
				
			case "userinfangroup":					
				queryString.append("lb.rank, ");
				queryString.append("u.username as name, ");
				queryString.append("lb.points, ");
				queryString.append("lb.potential_winnings ");
				queryString.append("from user_in_fangroup_game_leaderboard lb ");
				queryString.append("left join user u on u.user_id = lb.user ");
				queryString.append("where lb.game = ");
				queryString.append(game_id);
				queryString.append(" and lb.fangroup_id = ");
				
				// Get fangroup of  user
				StringBuffer fanQueryString = new StringBuffer("from Fan f where f.fangroup.competition = ");
				fanQueryString.append("(select competition_id from Game g where g.game_id = ");
				fanQueryString.append(game_id);
				fanQueryString.append(") and f.user.username = '");
				fanQueryString.append(username);
				fanQueryString.append("'");
				Query fanQuery = session.createQuery(fanQueryString.toString());
				
				List<Fan> result = fanQuery.list();
				if (result.isEmpty()) {
					log.error(username+" | User has not selected a fangroup for the competition for game "+game_id);
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
		query.addScalar("points");
		query.addScalar("potential_winnings");
		query.setResultTransformer(Transformers.aliasToBean(GameLeaderboardResponse.class));
		
		return query.list();
		
	}
	 
	
	
}
