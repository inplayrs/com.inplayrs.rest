package com.inplayrs.rest.service;


import com.inplayrs.rest.constants.GameType;
import com.inplayrs.rest.constants.LeaderboardType;
import com.inplayrs.rest.constants.State;
import com.inplayrs.rest.ds.Fan;
//import com.inplayrs.rest.ds.FanGroup;
//import com.inplayrs.rest.ds.FangroupCompLeaderboard;
//import com.inplayrs.rest.ds.FangroupGameLeaderboard;
import com.inplayrs.rest.ds.Game;
import com.inplayrs.rest.ds.GameEntry;
import com.inplayrs.rest.ds.GlobalCompLeaderboard;
import com.inplayrs.rest.ds.GlobalGameLeaderboard;
import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.PeriodOption;
import com.inplayrs.rest.ds.PeriodSelection;
import com.inplayrs.rest.ds.PoolCompLeaderboard;
import com.inplayrs.rest.ds.PoolGameEntry;
import com.inplayrs.rest.ds.PoolGameLeaderboard;
import com.inplayrs.rest.ds.PoolMember;
import com.inplayrs.rest.ds.User;
//import com.inplayrs.rest.ds.UserInFangroupCompLeaderboard;
//import com.inplayrs.rest.ds.UserInFangroupGameLeaderboard;
import com.inplayrs.rest.exception.DBException;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.InvalidStateException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.responseds.GameLeaderboardResponse;
import com.inplayrs.rest.responseds.GamePointsResponse;
import com.inplayrs.rest.responseds.GameWinnersResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.joda.time.LocalDateTime;
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
	private static final Logger log = LogManager.getLogger(GameService.class.getName());
	
	/*
	  * Retrieves a single period by the period_id
	  */
	public Period getPeriod(int period_id) {
		
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info(username+" | Getting period "+period_id);
		
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
		log.info(username+" | GET game/periods game_id="+game_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a list of Periods
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("FROM Period p where p.game.game_id = :game_id");
		
		// Filter periods by state
		queryString.append(" and p.state in (");
		queryString.append(State.PREPLAY).append(", ");
		queryString.append(State.TRANSITION).append(", ");
		queryString.append(State.INPLAY).append(", ");
		queryString.append(State.COMPLETE).append(", ");
		queryString.append(State.SUSPENDED).append(", ");
		queryString.append(State.NEVERINPLAY).append(") ");
		
		Query query = session.createQuery(queryString.toString());
		query.setParameter("game_id", game_id);
		
		return  query.list();
	 }
	


	@SuppressWarnings("unchecked")
	public List<PeriodSelection> getPeriodSelections(Integer game_id, String username) {
		
		log.info(username+" | Getting period selections for game "+game_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("from PeriodSelection ps where ps.gameEntry.game_id = :game_id ");
		queryString.append("and ps.gameEntry.user.username = :username");
		
		Query query = session.createQuery(queryString.toString());
		query.setParameter("game_id", game_id);
		query.setParameter("username", username);
		
		return query.list();
	}
	
	
	/*
	 * GET game/points
	 */
	public GamePointsResponse getGamePoints(Integer game_id, String username, Boolean includeSelections) {
		
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if (authed_user.equals(username)) {
			log.info(authed_user+" | GET game/points game_id="+game_id);
		} else {
			log.info(authed_user+" | GET game/points game_id="+game_id+" user="+username);
		}
		
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
		queryString.append("h2h_user.facebook_id as h2h_fbID, ");
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
		queryString.append("where u.username = :username");
		queryString.append(") as fangrp	on fangrp.competition = g.competition ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("fgl.game as game_id, ");
		queryString.append("count(distinct fgl.fangroup_id) as num_fangroups_entered ");
		queryString.append("from fangroup_game_leaderboard fgl ");
		queryString.append("where fgl.game = :game_id");	
		queryString.append(") as num_fangroups on num_fangroups.game_id = g.game_id ");
		
		queryString.append("left join ( ");
		queryString.append("select ");
		queryString.append("uifgl.fangroup_id, ");
		queryString.append("count(uifgl.user) as fangroup_pool_size ");
		queryString.append("from user_in_fangroup_game_leaderboard uifgl ");
		queryString.append("where uifgl.game = :game_id");
		queryString.append(" group by uifgl.fangroup_id");
		queryString.append(") as fangroup_pool on fangroup_pool.fangroup_id = fangrp.fangroup_id ");

		queryString.append("where ge.game = :game_id ");
		queryString.append("and u.username = :username");
		
		SQLQuery query = session.createSQLQuery(queryString.toString());
		query.setParameter("game_id", game_id);
		query.setParameter("username", username);
		
		query.addScalar("global_pot_size");
		query.addScalar("fangroup_pot_size");
		query.addScalar("h2h_pot_size");
		query.addScalar("global_winnings");
		query.addScalar("fangroup_winnings");
		query.addScalar("h2h_winnings");
		query.addScalar("total_winnings");
		query.addScalar("h2h_user");
		query.addScalar("h2h_fbID");
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
			log.info(username+" | No points found for game "+game_id);
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
		
		log.info(username+" | POST game/selections game_id="+game_id);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();		

		// GameEntry for the selections
		GameEntry gameEntry = new GameEntry();
		
		// Create game entry if we don't already have one
		Query gameEntryQuery = session.createQuery("from GameEntry ge where ge.game.game_id = :game_id "+
										   		   "and ge.user.username = :username");
		
		gameEntryQuery.setParameter("game_id", game_id);
		gameEntryQuery.setParameter("username", username);
		
		List<GameEntry> result = gameEntryQuery.list();
		
		if (result.isEmpty()) {
			
			log.info(username+" | This is user's initial submit for game "+game_id+", creating a new game entry");
			
			// Check whether any periods are suspended as can't submit selections if this is the case
			for (PeriodSelection ps : periodSelections) {
				Period period = (Period) session.load(Period.class, ps.getPeriod_id());
				
				if (period.getState() == State.SUSPENDED) {
					log.info(username+" | Cannot post initial selections, one or more events are currently suspended for game "+game_id);
					session.delete(gameEntry);
					throw new InvalidStateException(new RestError(2202, "One or more events are currently suspended, please try again later!"));
				}
			}
			
			// Create a new GameEntry
			Game g = (Game) session.get(Game.class, game_id);	

			if (g == null) {
				log.error(username+" | Game "+game_id+" does not exist");
				throw new InvalidParameterException(new RestError(2200, "Game "+game_id+" does not exist"));
			}
			
			/*
			 * NO LONGER CHECKING FOR FANGROUP as removing fangroup from game
			 * TODO: Remove fangroup code
			 * 
			// Check that user has a fangroup before creating a game entry
			StringBuffer fangroupQueryString = new StringBuffer("from Fan f where f.user.username = :username ");
			fangroupQueryString.append("and f.fangroup.competition.comp_id = :comp_id");
			
			Query fangroupQuery = session.createQuery(fangroupQueryString.toString());
			fangroupQuery.setParameter("username", username);
			fangroupQuery.setParameter("comp_id", g.getCompetition_id());
			
			Fan fan = (Fan)fangroupQuery.uniqueResult();
			if (fan == null) {
				log.info(username+" | Cannot post selections, no fangroup selected for competition "+g.getCompetition_id());
				throw new InvalidStateException(new RestError(2201, "Please select a fangroup for this competition before entering!"));
			}
			*/
			
			// Get user object
			Query userQuery = session.createQuery("FROM User u WHERE u.username = :username");
			userQuery.setParameter("username", username);
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
		
			synchronized (this) {
				// Increment num players in game
				g.setNum_players(g.getNum_players()+1);
				
				// Always increase global pot size
				g.setGlobal_pot_size(g.getGlobal_pot_size() + g.getStake());
				session.update(g);
			}
			
			/*
			// Only increase fangroup pot size if it's not a late entry
			if (g.getState() == State.PREPLAY  || g.getState() == State.TRANSITION) {
				log.info(username+" | Increasing fangroup_pot_size for game "+game_id);
				g.setFangroup_pot_size(g.getFangroup_pot_size() + g.getStake());
			}
			*/
			
			

			/*
			 * NO LONGER HAVE FANGROUP
			 * TODO: Remove code
			FanGroup fanGroup = fan.getFangroup();
			*/
			
			// Add user to global_game_leaderboard
			log.info(username+" | Adding user to global_game_leaderboard for game "+game_id);
			GlobalGameLeaderboard globalGameLeaderboard = new GlobalGameLeaderboard();
			globalGameLeaderboard.setGame(g);
			globalGameLeaderboard.setUser(usr);
			//globalGameLeaderboard.setFangroup(fanGroup);
			globalGameLeaderboard.setPoints(0);
			globalGameLeaderboard.setPotential_winnings(0);
			//globalGameLeaderboard.setFangroupName(fanGroup.getName()); // TODO - Deco when field removed
			session.save(globalGameLeaderboard);
			
			/* NO LONGER ADDING TO FANGROUP LEADERBOARDS as removing fangroup from game
			 * TODO: Remove fangroup code
			 * 
			// See if entry exists in fangroup_game_leaderboard, and create entry if not
			StringBuffer fglQueryString = new StringBuffer("FROM FangroupGameLeaderboard fgl where ");
			fglQueryString.append("fgl.game.game_id = :game_id and fgl.fangroup.fangroup_id = :fangroup_id");
			Query fglQuery = session.createQuery(fglQueryString.toString());
			fglQuery.setParameter("game_id", g.getGame_id());
			fglQuery.setParameter("fangroup_id", fanGroup.getFangroup_id());
			
			FangroupGameLeaderboard fgl = (FangroupGameLeaderboard) fglQuery.uniqueResult();
		
			if (fgl == null) {
				// Create new FangroupGameLeaderboard entry
				log.info(username+" | Adding user to fangroup_game_leaderboard for game "+game_id);
				fgl = new FangroupGameLeaderboard();
				fgl.setGame(g);
				fgl.setFangroup(fanGroup);
				fgl.setFangroupName(fanGroup.getName()); // TODO - Deco when field removed
				fgl.setNumPlayers(1); 	// TODO - race condition - fix
				session.save(fgl);
			} else {
				log.info(username+" | User is already in fangroup_game_leaderboard for game "+game_id);
				fgl.setNumPlayers(fgl.getNumPlayers()+1);
				session.save(fgl);
			}
			
			// Add user to user_in_fangroup_game_leaderboard
			log.info(username+" | Adding user to user_in_fangroup_game_leaderboard for game "+game_id);
			UserInFangroupGameLeaderboard uifgl = new UserInFangroupGameLeaderboard();
			uifgl.setGame(g);
			uifgl.setUser(usr);
			uifgl.setFangroup(fanGroup);
			uifgl.setFangroupName(fanGroup.getName());
			session.save(uifgl);
			*/
			
			// See if user is already in global_comp_leaderboard, and add if not
			StringBuffer gclQueryString = new StringBuffer("FROM GlobalCompLeaderboard gcl where ");
			gclQueryString.append("gcl.competition.comp_id = :comp_id and ");
			gclQueryString.append("gcl.user.user_id = :user_id");
			Query gclQuery = session.createQuery(gclQueryString.toString());
			gclQuery.setParameter("comp_id", g.getCompetition_id());
			gclQuery.setParameter("user_id", usr.getUser_id());
			
			GlobalCompLeaderboard gcl = (GlobalCompLeaderboard) gclQuery.uniqueResult();
			
			if (gcl == null) {
				log.info(username+" | Adding user to global_comp_leaderboard for comp "+g.getCompetition_id());
				gcl = new GlobalCompLeaderboard();
				gcl.setCompetition(g.getCompetition());
				gcl.setUser(usr);
				//gcl.setFangroup(fanGroup);
				//gcl.setFangroupName(fanGroup.getName());
				session.save(gcl);
			} else {
				log.info(username+" | User is already in global_comp_leaderboard for comp "+g.getCompetition_id());
			}
			
			
			/*
			 * NO LONGER ADDING TO FANGROUP LEADERBOARDS as removing fangroup from game
			 * TODO: Remove fangroup code
			 * 
			// See if entry exists in fangroup_comp_leaderboard, and create entry if not
			StringBuffer fclQueryString = new StringBuffer("FROM FangroupCompLeaderboard fcl where ");
			fclQueryString.append("fcl.competition.comp_id = :comp_id and ");
			fclQueryString.append("fcl.fangroup.fangroup_id = :fangroup_id");
			Query fclQuery = session.createQuery(fclQueryString.toString());
			fclQuery.setParameter("comp_id", g.getCompetition_id());
			fclQuery.setParameter("fangroup_id", fanGroup.getFangroup_id());
			
			FangroupCompLeaderboard fcl = (FangroupCompLeaderboard) fclQuery.uniqueResult();
			
			if (fcl == null) {
				log.info(username+" | Adding user to fangroup_comp_leaderboard for comp "+g.getCompetition_id());
				fcl = new FangroupCompLeaderboard();
				fcl.setCompetition(g.getCompetition());
				fcl.setFangroup(fanGroup);
				fcl.setFangroupName(fanGroup.getName());
				session.save(fcl);
			} else {
				log.info(username+" | User is already in fangroup_comp_leaderboard for comp "+g.getCompetition_id());
			}
			
			
			// See if entry exists in user_in_fangroup_comp_leaderboard, and create entry if not
			StringBuffer uifclQueryString = new StringBuffer("FROM UserInFangroupCompLeaderboard uifcl where ");
			uifclQueryString.append("uifcl.competition.comp_id = :comp_id and ");
			uifclQueryString.append("uifcl.user.user_id = :user_id");
			Query uifclQuery = session.createQuery(uifclQueryString.toString());
			uifclQuery.setParameter("comp_id", g.getCompetition_id());
			uifclQuery.setParameter("user_id", usr.getUser_id());
			
			UserInFangroupCompLeaderboard uifcl = (UserInFangroupCompLeaderboard) uifclQuery.uniqueResult();
			
			if (uifcl == null) {
				log.info(username+" | Adding user to user_in_fangroup_comp_leaderboard for comp "+g.getCompetition_id());
				uifcl = new UserInFangroupCompLeaderboard();
				uifcl.setCompetition(g.getCompetition());
				uifcl.setUser(usr);
				uifcl.setFangroup(fanGroup);
				uifcl.setFangroupName(fanGroup.getName());
				session.save(uifcl);
			} else {
				log.info(username+" | User is already in user_in_fangroup_comp_leaderboard for comp "+g.getCompetition_id());
			}
			*/
			
			// Add game entry for any pools the user is in, and add to pool leaderboards
			// but only if this is not a late entry
			if (g.getState() == State.PREPLAY  || g.getState() == State.TRANSITION) {
				Query poolMemberQuery = session.createQuery("from PoolMember pm where pm.user.username = :username");
				poolMemberQuery.setParameter("username", username);
				List <PoolMember> poolMembers = poolMemberQuery.list();
				for (PoolMember poolMember: poolMembers) {
					// Add game entry
					log.info(username+" | Adding game entry to pool "+poolMember.getPool().getPool_id()+
										 " for game "+gameEntry.getGame_id());
					PoolGameEntry poolGameEntry = new PoolGameEntry();
					poolGameEntry.setGameEntry(gameEntry);
					poolGameEntry.setPoolMember(poolMember);
					session.save(poolGameEntry);
					
					// Add to Pool Game Leaderboard
					log.info(username+" | Adding user to pool_game_leaderboard for game "+gameEntry.getGame_id()+
							" and pool "+poolMember.getPool().getPool_id());
					PoolGameLeaderboard poolGameLeaderboard = new PoolGameLeaderboard();
					poolGameLeaderboard.setPool(poolMember.getPool());
					poolGameLeaderboard.setGame(g);
					poolGameLeaderboard.setUser(usr);
					session.save(poolGameLeaderboard);
					
					// See if user is already a member of pool comp leaderboard, and add if not
					StringBuffer pclQueryString = new StringBuffer("FROM PoolCompLeaderboard pcl where ");
					pclQueryString.append("pcl.competition.comp_id = :comp_id and pcl.pool.pool_id = :pool_id ");
					pclQueryString.append("and pcl.user.username = :username");
					
					Query pclQuery = session.createQuery(pclQueryString.toString());
					pclQuery.setParameter("comp_id", g.getCompetition_id());
					pclQuery.setParameter("pool_id", poolMember.getPool().getPool_id());
					pclQuery.setParameter("username", usr.getUsername());
					
					PoolCompLeaderboard poolCompLeaderboard = (PoolCompLeaderboard) pclQuery.uniqueResult();
					if (poolCompLeaderboard == null) {
						log.info(username+" | Adding user to pool_comp_leaderboard for comp "+g.getCompetition_id()+
								" and pool "+poolMember.getPool().getPool_id());
						poolCompLeaderboard = new PoolCompLeaderboard();
						poolCompLeaderboard.setCompetition(g.getCompetition());
						poolCompLeaderboard.setPool(poolMember.getPool());
						poolCompLeaderboard.setUser(usr);
						session.save(poolCompLeaderboard);
					} else {
						log.info(username+" | User is already in pool_comp_leaderboard for comp "+g.getCompetition_id()+
								" and pool "+poolMember.getPool().getPool_id());
					}
				}
			}

		} else {
			// get the game entry
			gameEntry = (GameEntry) result.get(0);
		}
		
		
		for (PeriodSelection ps : periodSelections) {
			// Set the GameEntry for the period selection
			ps.setGameEntry(gameEntry);
			ps.setGame_entry_id(gameEntry.getGame_entry_id());

			Period period = (Period) session.load(Period.class, ps.getPeriod_id());
			ps.setPeriod(period);
			
			PeriodOption period_option = (PeriodOption) session.load(PeriodOption.class, ps.getPeriod_option_id());
			ps.setPeriod_option(period_option);
			
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
			
			// Set awarded_points for Quiz game type only
			if (ps.getPeriod().getGame().getGame_type() == GameType.QUIZ) {
				if (ps.getAwarded_points() > 0) {
					// Can only set awarded points for quiz answers that are correct
					if (ps.getPeriod_option().getState() == 0) {
						throw new InvalidStateException(new RestError(2203,username+" | Attempting to set awarded points as "
								+ps.getAwarded_points()+" when the state of period_option "+ps.getPeriod_option().getPo_id()+
								" is "+ps.getPeriod_option().getState()));
					} else if (ps.getAwarded_points() > ps.getPeriod_option().getPoints()) {
						// Can't award more points than available for that option
						throw new InvalidStateException(new RestError(2204,username+" Attempting to set awarded points as "
								+ps.getAwarded_points()+" when the points for period_option "+ps.getPeriod_option().getPo_id()+
								" is "+ps.getPeriod_option().getPoints()));
					}
				}
			} else if (ps.getAwarded_points() > 0){
				ps.setAwarded_points(0);
			}
			
			Query periodSelectionQuery = session.createQuery("from PeriodSelection ps where ps.gameEntry.game_entry_id = :game_entry_id "+
															 "and ps.period.period_id = :period_id");
			periodSelectionQuery.setParameter("game_entry_id", gameEntry.getGame_entry_id());
			periodSelectionQuery.setParameter("period_id", ps.getPeriod_id());
			
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
					log.info(username+" | Could not update period selection, period "+ps.getPeriod_id()+" is no longer in preplay/transition");
				}
			} else {
				// Update if pre-play/transition and not cashed out
				PeriodSelection currentSelection = psqResult.get(0);
				int period_state = currentSelection.getPeriod().getState();
				if (period_state == State.PREPLAY || period_state == State.TRANSITION) {
					if (currentSelection.isCashed_out()) {
						log.info(username+" | User has already banked their points for period "+ps.getPeriod_id());
					} else {
						log.info(username+" | Updating selection for period "+ps.getPeriod_id());
						currentSelection.setSelection(ps.getSelection());
						currentSelection.setPotential_points(ps.getPotential_points());
						currentSelection.setAwarded_points(ps.getAwarded_points());
						try {
							session.update(currentSelection);
						}
						catch (Exception e) {
							log.error(username+" | Failed to update period selection for period "+ps.getPeriod_id());
							throw new DBException(new RestError(1000, "Failed to update period selection"));
						}
					}
					
				} else {
					log.info(username+" | Could not update period selection, period "+ps.getPeriod_id()+" is no longer in preplay/transition");
				}
				
			}
						
		}
		
		return null;
	}


	
	/*
	 * POST game/period/bank
	 */
	public PeriodSelection bankPeriodPoints(Integer period_id, String username) {
		
		log.info(username+" | POST game/period/bank period_id="+period_id);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		StringBuffer queryString = new StringBuffer("from PeriodSelection ps where ps.period.period_id = :period_id ");
		queryString.append("and ps.gameEntry.user.username = :username");
		
		Query query = session.createQuery(queryString.toString());
		query.setParameter("username", username);
		query.setParameter("period_id", period_id);
		
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
								String type, String username, Integer pool_id) {

		log.info(username+" | GET game/leaderboard type="+type+" game_id="+game_id);
		
		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("select ");
		
		// Build query string based on type of leaderboard
		switch(type) {
			case LeaderboardType.GLOBAL:
				queryString.append("lb.rank, ");
				queryString.append("u.username as name, ");
				queryString.append("lb.points, ");
				queryString.append("lb.potential_winnings ");
				queryString.append("from global_game_leaderboard lb ");
				queryString.append("left join user u on u.user_id = lb.user ");
				queryString.append("where lb.game = :game_id");
				break;
				
			case LeaderboardType.FANGROUP:
				queryString.append("lb.rank, ");
				queryString.append("lb.fangroup_name as name, ");
				queryString.append("lb.avg_points as points, ");
				queryString.append("lb.potential_winnings ");
				queryString.append("from fangroup_game_leaderboard lb ");
				queryString.append("where lb.game = :game_id");
				break;
				
			case LeaderboardType.USER_IN_FANGROUP:					
				queryString.append("lb.rank, ");
				queryString.append("u.username as name, ");
				queryString.append("lb.points, ");
				queryString.append("lb.potential_winnings ");
				queryString.append("from user_in_fangroup_game_leaderboard lb ");
				queryString.append("left join user u on u.user_id = lb.user ");
				queryString.append("where lb.game = :game_id ");;
				queryString.append("and lb.fangroup_id = ");
				
				// Get fangroup of  user
				StringBuffer fanQueryString = new StringBuffer("from Fan f where f.fangroup.competition.comp_id = ");
				fanQueryString.append("(select competition_id from Game g where g.game_id = :game_id");
				fanQueryString.append(") and f.user.username = :username");
				Query fanQuery = session.createQuery(fanQueryString.toString());
				fanQuery.setParameter("username", username);
				fanQuery.setParameter("game_id", game_id);
				
				List<Fan> result = fanQuery.list();
				if (result.isEmpty()) {
					log.error(username+" | User has not selected a fangroup for the competition for game "+game_id);
					return null;
				} else {
					Fan fan = result.get(0);
					queryString.append(fan.getFangroup_id());
				}
				break;
				
			case LeaderboardType.POOL:
				queryString.append("lb.rank as rank, ");
				queryString.append("u.username as name, ");
				queryString.append("lb.points, ");
				queryString.append("lb.potential_winnings ");
				queryString.append("from pool_game_leaderboard lb ");
				queryString.append("left join user u on u.user_id = lb.user ");
				queryString.append("where lb.game = :game_id ");
				queryString.append("and lb.pool = :pool_id ");
				break;
				
			default: return null;	
		}

		// Only return top 100 users 
		queryString.append(" and lb.rank >= 1 ORDER BY lb.rank LIMIT 100");
		
		SQLQuery query = session.createSQLQuery(queryString.toString());
		query.setParameter("game_id", game_id);
		if (queryString.toString().contains(":pool_id")) {
			query.setParameter("pool_id", pool_id);
		}
		query.addScalar("rank");
		query.addScalar("name");
		query.addScalar("points");
		query.addScalar("potential_winnings");
		query.setResultTransformer(Transformers.aliasToBean(GameLeaderboardResponse.class));
		
		return query.list();
		
	}
	 
	
	
	/*
	 * GET game/winners
	 */
	public List<GameWinnersResponse> getGameWinners(Integer game_id) {
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (game_id != null) {
			log.info(username+" | GET game/winners game_id="+game_id);
		} else {
			log.info(username+" | GET game/winners");
		}
				
		// Retrieve session from Hibernate, create query (HQL) and return a list of fangroups
		Session session = sessionFactory.getCurrentSession();
		
		// Get winners of competitions
		StringBuffer queryString = new StringBuffer("select ");
		queryString.append("ggl.game.game_id as game_id, ");
		queryString.append("ggl.game.name as game, ");
		queryString.append("ggl.game.competition.category.cat_id, ");
		queryString.append("ggl.game.end_date as game_end_date, ");
		queryString.append("ggl.game.competition.comp_id as comp_id, ");
		queryString.append("ggl.game.inplay_type as inplay_type, ");
		queryString.append("ggl.game.state as state, ");
		queryString.append("ggl.game.game_type as game_type, ");
		queryString.append("ggl.user.username as user from GlobalGameLeaderboard ggl where ggl.rank = 1");
		
		// Filter competitions by state
		queryString.append(" and ggl.game.state in (");
		queryString.append(State.COMPLETE).append(", ");
		queryString.append(State.ARCHIVED).append(")");
		
		// Filter by game ID if specified
		if (game_id != null) {
			queryString.append(" and ggl.game.game_id = :game_id");
		}
		
		// order by game end date
		queryString.append(" ORDER BY ggl.game.end_date DESC");
		
		Query query = session.createQuery(queryString.toString());
		if (game_id != null) {
			query.setParameter("game_id", game_id);
		}
		
		//return first 100
		query.setMaxResults(100);
		
		List<GameWinnersResponse> response = new ArrayList<>();
		
		GameWinnersResponse gwr = new GameWinnersResponse();
		
		// Iterate over result set
		@SuppressWarnings("rawtypes")
		Iterator gameWinners = query.list().iterator();
		while(gameWinners.hasNext()) {
			// Process each competition winner and add to response object
			Object[] row = (Object[]) gameWinners.next();
			Integer gameID = (Integer) row[0];
			String game = (String) row[1];
			Integer cat_id = (Integer) row[2];
			LocalDateTime gameEndDate = (LocalDateTime) row[3];
			Integer comp_id = (Integer) row[4];
			Integer inplay_type = (Integer) row[5];
			Integer state = (Integer) row[6];
			Integer game_type = (Integer) row[7];
			String user = (String) row[8];
		
			// If this winner is for the same game that we're processing, 
			// add to the list of winners
			if (gameID == gwr.getGame_id()) {
				List<String> winners = new ArrayList<String>();
				
				if (!gwr.getWinners().isEmpty()) {
					winners = gwr.getWinners();	
				}
				winners.add(user);
				gwr.setWinners(winners);
			} 
			else {
				// Add the previous winner response object to the result set
				if (gwr.getGame_id() != null) {
					response.add(gwr);
					gwr = new GameWinnersResponse();
				}
				
				// Set the new winner response object
				gwr.setGame_id(gameID);
				gwr.setGame(game);
				gwr.setCategory_id(cat_id);
				gwr.setGameEndDate(gameEndDate);
				gwr.setComp_id(comp_id);
				gwr.setInplay_type(inplay_type);
				gwr.setState(state);
				gwr.setGame_type(game_type);
				
				List<String> winners = new ArrayList<String>();
				winners.add(user);
				gwr.setWinners(winners);
			}
		}
		// Add final result to response
		if (gwr.getGame_id() != null) {
			response.add(gwr);
		}
		
		return  response;
		
				
	}
	
}
