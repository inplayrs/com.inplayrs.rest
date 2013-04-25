package com.inplayrs.rest.service;


import com.inplayrs.rest.ds.Game;
import com.inplayrs.rest.ds.GameEntry;
import com.inplayrs.rest.ds.Period;
import com.inplayrs.rest.ds.PeriodSelection;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.responseds.GamePointsResponse;

import java.util.List;
import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

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
	

	
	@SuppressWarnings("unchecked")
	public List<PeriodSelection> getPeriodSelections(Integer game_id, String username) {
		
		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("from PeriodSelection ps where ps.gameEntry.game_id = ");
		queryString.append(game_id.toString());
		queryString.append(" and ps.gameEntry.user = '");
		queryString.append(username);
		queryString.append("'");
		
		Query query = session.createQuery(queryString.toString());
		
		return query.list();
	}
	
	
	
	public GamePointsResponse getGamePoints(Integer game_id, String username) {
		
		// Retrieve session from Hibernate, create query (HQL) and return a GamePointsResponse
		Session session = sessionFactory.getCurrentSession(); 
		
		StringBuffer queryString = new StringBuffer("select ");
		queryString.append("g.global_pot_size, ");
		queryString.append("g.fangroup_pot_size, ");
		queryString.append("h2h.h2h_pot_size, ");
		queryString.append("ge.total_winnings, ");
		queryString.append("h2h_ggl.user as h2h_user, ");
		queryString.append("h2h_ggl.points as h2h_points, ");
		queryString.append("ggl.fangroup_name as fangroup_name, ");
		queryString.append("ggl.points, ");
		queryString.append("ggl.rank as global_rank, ");
		queryString.append("fgl.rank as fangroup_rank, ");
		queryString.append("uifgl.rank as user_in_fangroup_rank ");
		queryString.append("from  ");
		queryString.append("game_entry ge ");
		queryString.append("left join game g on g.game_id = ge.game ");
		queryString.append("left join h2h_pool h2h on ( (h2h.game_entry_1 = ge.game_entry_id) or ");
		queryString.append("(h2h.game_entry_2 = ge.game_entry_id) ) ");
		queryString.append("left join global_game_leaderboard ggl on (ggl.game = ge.game and ggl.user = ge.user) ");
		queryString.append("left join fangroup_game_leaderboard fgl on (fgl.game = ge.game and fgl.fangroup_id = ggl.fangroup_id) ");
		queryString.append("left join user_in_fangroup_game_leaderboard uifgl on (uifgl.game = ge.game and uifgl.user = ge.user) ");
		queryString.append("left join global_game_leaderboard h2h_ggl ON (h2h_ggl.game = ge.game and h2h_ggl.user = ");
		queryString.append("(CASE WHEN h2h.game_entry_1 = ge.game_entry_id then h2h.user_2 ");
		queryString.append(" WHEN h2h.game_entry_2 = ge.game_entry_id then h2h.user_1 ELSE null END) ) ");
		queryString.append(" where ge.game = ").append(game_id.toString());
		queryString.append(" and ge.user = '").append(username).append("'");
		
		SQLQuery query = session.createSQLQuery(queryString.toString());
		
		query.addScalar("global_pot_size");
		query.addScalar("fangroup_pot_size");
		query.addScalar("h2h_pot_size");
		query.addScalar("total_winnings");
		query.addScalar("h2h_user");
		query.addScalar("h2h_points");
		query.addScalar("fangroup_name");
		query.addScalar("points");
		query.addScalar("global_rank");
		query.addScalar("fangroup_rank");
		query.addScalar("user_in_fangroup_rank");
		query.setResultTransformer(Transformers.aliasToBean(GamePointsResponse.class));
				
		@SuppressWarnings("unchecked")
		List<GamePointsResponse> result = query.list();
		
		if (result.isEmpty()) {
			return null;
		} else {	
			GamePointsResponse gpr = (GamePointsResponse) result.get(0);
			gpr.setPeriodSelections(getPeriodSelections(game_id, username));		
			return gpr;
		}
	}
	
	


	/*
	 *  Add period selections for a game
	 */
	public Integer addGamePeriodSelections(Integer game_id, String username, PeriodSelection[] periodSelections) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// GameEntry for the selections
		GameEntry gameEntry = new GameEntry();
		
		// Create game entry if we don't already have one
		Query gameEntryQuery = session.createQuery("FROM GameEntry ge where ge.game = "+game_id.toString()+
										   " and ge.user = '"+username+"'");
		
		@SuppressWarnings("unchecked")
		List<GameEntry> result = gameEntryQuery.list();
		
		if (result.isEmpty()) {
			// Create a new GameEntry
			gameEntry.setGame_id(game_id);
			gameEntry.setUsername(username);
			gameEntry.setGame((Game) session.load(Game.class, game_id));
			gameEntry.setUser((User) session.load(User.class, username));
			session.save(gameEntry);
		} else {
			// get the game_entry_id
			gameEntry = (GameEntry) result.get(0);
		}
		
		
		for (PeriodSelection ps : periodSelections) {
			// Set the GameEntry for the period selection
			ps.setGameEntry(gameEntry);
			ps.setGame_entry_id(gameEntry.getGame_entry_id());

			ps.setPeriod((Period) session.load(Period.class, ps.getPeriod_id()));
				
			Query periodSelectionQuery = session.createQuery("from PeriodSelection ps where ps.gameEntry = "+
															 gameEntry.getGame_entry_id()+
															 " and ps.period = "+
															 ps.getPeriod_id());
			
			@SuppressWarnings("unchecked")
			List <PeriodSelection> psqResult = periodSelectionQuery.list();
			
			if (psqResult.isEmpty()) {
				// Add the new period selection if state is preplay/transition/inplay
				if (ps.getPeriod().getState() >= -1 && ps.getPeriod().getState() <=1) {
					System.out.println("adding new selection");
					session.save(ps);
				} else {
					System.out.println("Could not update Period Selection, Period is no longer in preplay/transition/inplay");
				}
			} else {
				PeriodSelection currentSelection = psqResult.get(0);
				// Update if pre-play/transition and not cashed out
				int period_state = currentSelection.getPeriod().getState();
				if (period_state == -1 || period_state == 0) {
					if (currentSelection.isCashed_out()) {
						System.out.println("Could not update Period Selection, user has already banked their points");
					} else {
						System.out.println("updating existing selection");
						currentSelection.setSelection(ps.getSelection());
						session.update(currentSelection);
					}
					
				} else {
					System.out.println("Could not update Period Selection, Period is no longer in preplay/transition/inplay");
				}
				
			}
			
			
			
		}
		
		return null;
	}


	public PeriodSelection bankPeriodPoints(Integer period_id, String username) {
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		StringBuffer queryString = new StringBuffer("from PeriodSelection ps where ps.period = ");
		queryString.append(period_id);
		queryString.append(" and ps.gameEntry.user = '");
		queryString.append(username);
		queryString.append("'");
		
		Query query = session.createQuery(queryString.toString());
		
		@SuppressWarnings("unchecked")
		List<PeriodSelection> result = query.list();
		
		if (result.isEmpty()) {		
			throw new RuntimeException("No PeriodSelection for this User and Period found.  Cannot bank points."); 
		} else {
			PeriodSelection ps = result.get(0);
			// Can't bank points if already banked
			if (ps.isCashed_out()) {
				throw new RuntimeException("User has already banked points for this period");
			}
			
			Period period = ps.getPeriod();
			
			// Can only bank points if period is still in transition/inplay
			if (period.getState() == 0 || period.getState() == 1) {
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
					
				// Return null for alpha
				return null;
				//return ps;
				
			} else {
				throw new RuntimeException("Cannot bank points, period is no longer in play");
			}

		}
			
	}
	 
	
	
}
