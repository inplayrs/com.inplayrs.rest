package com.inplayrs.rest.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.constants.Threshold;
import com.inplayrs.rest.ds.Competition;
import com.inplayrs.rest.ds.Fan;
import com.inplayrs.rest.ds.FanGroup;
import com.inplayrs.rest.ds.Game;
import com.inplayrs.rest.ds.Motd;
import com.inplayrs.rest.ds.Pat;
import com.inplayrs.rest.ds.Pool;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.ds.UserInvite;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.InvalidStateException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.responseds.UserLeaderboardResponse;
import com.inplayrs.rest.responseds.UserStatsResponse;
import com.inplayrs.rest.security.PasswordHash;
import com.inplayrs.rest.util.IPUtil;


/*
 * Service for processing user info
 */
@Service("userService")
@Transactional
public class UserService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	//get log4j handler
	private static final Logger log = Logger.getLogger("APILog");
	
	
	/*
	 * POST user/register (Username & Password registration)
	 */
	public User registerUser(String username, String password, String email, String timezone, 
							 String deviceID, Boolean pushActive, String gcID, String gcUsername, 
							 String fbID, String fbUsername, String fbEmail, String fbFullName) {
		
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(authed_user+" | Registering new user: "+username);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Check if username is a banned word 
		if (IPUtil.isBadWord(username)) {
			log.debug(authed_user+" | Username "+username+" is not available");
			throw new InvalidStateException(new RestError(2302, "Username "+username+" is not available"));
		}	
		
		// See if user already exists
		Query query = session.createQuery("FROM User u WHERE u.username = :username");
		query.setParameter("username", username);
		query.setCacheable(true);
		query.setCacheRegion("user");
		User usr = (User) query.uniqueResult();
		
		if (usr == null) {
			usr = new User();
			usr.setUsername(username);
			try {
				usr.setPassword_hash(PasswordHash.createHash(password));
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				log.error(authed_user+" | Unable to create password hash");
				e.printStackTrace();
				throw new RuntimeException("Unable to create password hash");
			}
			if (email != null) {
				// Check that an existing user doesn't have the same email
				Query emailQuery = session.createQuery("select count(*) from User where email = :email");
				emailQuery.setParameter("email", email);
				
				Integer usersWithSameEmail = ( (Long) emailQuery.iterate().next() ).intValue();
				
				if (usersWithSameEmail > 0) {
					log.error(authed_user+" | Email address "+email+" is already registered");
					throw new InvalidStateException(new RestError(2301, "Email address "+email+" is already registered"));
				} else {
					usr.setEmail(email);
				}
			}
			
			if (timezone != null) {
				usr.setTimezone(timezone);
			}
			
			if (deviceID != null) {
				usr.setDeviceID(deviceID);
			}
			
			// Default push_active to true if not specified when registering
			if (pushActive != null) {
				usr.setPushActive(pushActive);
			} else {
				usr.setPushActive(true);
			}
			
			if (gcID != null) {
				usr.setGamecenter_id(gcID);
			}
			
			if (gcUsername != null) {
				usr.setGamecenter_username(gcUsername);
			}
			
			if (fbID != null) {
				usr.setFacebook_id(fbID);
			}
			
			if (fbUsername != null) {
				usr.setFacebook_username(fbUsername);
			}
			
			if (fbEmail != null) {
				usr.setFacebook_email(fbEmail);
				if (usr.getEmail() == null) {
					usr.setEmail(fbEmail);
				}
			}
			
			if (fbFullName != null) {
				usr.setFacebook_full_name(fbFullName);
			}
				
			session.save(usr);
			return usr;
			
		} else {
			log.error(authed_user+" | Username "+username+" is not available");
			throw new InvalidStateException(new RestError(2300, "Username "+username+" is not available"));
		}
		
	}
	
	
	/*
	 * POST user/register (Game Center registration)
	 */
	public User registerGCUser(String gcID, String gcUsername, String password, String email, 
								String timezone, String deviceID, Boolean pushActive) {
		
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(authed_user+" | Registering new user with gamecenter_id: "+gcID);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Check if someone is already registered with that gamecenter_id
		Query query = session.createQuery("select 1 from User u WHERE u.gamecenter_id = :gcID");
		query.setParameter("gcID", gcID);
		
		if (query.uniqueResult() != null) {
			throw new InvalidParameterException(
					new RestError(2306, "User with gamecenter_id "+gcID+" is already registered")
			);
		}
		
		// Check if we can use the gamecenter_id as the username (take first 14 characters)
		String shortenedGCusername;
		if (gcUsername.length() > 14) {
			shortenedGCusername = gcUsername.substring(0, 14);
		} else {
			shortenedGCusername = gcUsername;
		}
		String username = _getNextAvailableUsername(shortenedGCusername, 0);
		
		log.debug(authed_user+" | GC Username provided: "+gcUsername+", next available username: "+username);
		
		return registerUser(username, password, email, timezone, deviceID, pushActive, gcID, gcUsername, 
							null, null, null, null);
	}
	
	
	/*
	 * POST user/register (Facebook registration)
	 */
	public User registerFBUser(String fbID, String fbUsername, String fbEmail, String fbFullName, String password, 
			String email, String timezone, String deviceID, Boolean pushActive) {
		
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(authed_user+" | Registering new user with facebook_id: "+fbID);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Check if someone is already registered with that facebook_id
		Query query = session.createQuery("select 1 from User u WHERE u.facebook_id = :fbID");
		query.setParameter("fbID", fbID);
		if (query.uniqueResult() != null) {
			throw new InvalidParameterException(
					new RestError(2309, "User with facebook_id "+fbID+" is already registered")
			);
		}

		String username = null;
		
		if (fbUsername != null) {
			// Take first 14 characters of username and check if available
			String shortenedFBusername;
			if (fbUsername.length() > 14) {
				shortenedFBusername = fbUsername.substring(0, 14);
			} else {
				shortenedFBusername = fbUsername;
			}
			username = _getNextAvailableUsername(shortenedFBusername, 0);
		} else {
			// Take first 14 characters of full name and check if available
			String fbFullNameNoSpaces = fbFullName.replaceAll("\\s+", "");
			String shortenedFBName;
			if (fbFullNameNoSpaces.length() > 14) {
				shortenedFBName = fbFullNameNoSpaces.substring(0, 14);
			} else {
				shortenedFBName = fbFullNameNoSpaces;
			}
			username = _getNextAvailableUsername(shortenedFBName, 0);
		}
				
		return registerUser(username, password, email, timezone, deviceID, pushActive, null, null, 
							fbID, fbUsername, fbEmail, fbFullName);
	}
	
	

	
	
	
	/*
	 * Get the next available username based on input name
	 */
	public String _getNextAvailableUsername(String username, int attempt) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Cannot have a negative value for attempt
		if (attempt < 0) {
			log.error("_getNextAvailableUsername - Attempt must be >= 0");
			throw new InvalidParameterException(new RestError(2307, "Attempt must be >= 0"));
		}
		
		// Allow max 9 attempts to find an alternative username
		if (attempt > 9) {
			log.error("_getNextAvailableUsername - Maximum of 9 attempts reached to find an available username!");
			throw new InvalidParameterException(new RestError(2308, "Your username is already taken, please try registering via email instead"));
		}
		
		if (attempt > 0) {
			// Append attempt number to username to see if that name is available
			Query usrQuery = session.createQuery("select count(*) from User u WHERE u.username = :username_attempt");
			usrQuery.setParameter("username_attempt", username+attempt);
			Long exists = (Long) usrQuery.uniqueResult();
			if (exists > 0) {
				return _getNextAvailableUsername(username, attempt+1);
			} else {
				return username + attempt;
			}
		} else {
			// First attempt, see if that username is available
			Query usrQuery = session.createQuery("select count(*) from User u WHERE u.username = :username");
			usrQuery.setParameter("username", username);
			Long exists = (Long) usrQuery.uniqueResult();
			if (exists > 0) {
				return _getNextAvailableUsername(username, attempt+1);
			} else {
				return username;
			}
	
		}
		
	}
	
	
	/*
	 * POST user/account/update
	 */
	public User updateAccount(String username, String password, String email, String timezone, String deviceID, 
							  Boolean pushActive, String newUsername, String gcID, String gcUsername, 
							  String fbID, String fbUsername, String fbEmail, String fbFullName) {

		log.debug(username+" | Updating user account");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();

		// Get details of the user
		Query query = session.createQuery("FROM User u WHERE u.username = :username");
		query.setParameter("username", username);
		query.setCacheable(true);
		query.setCacheRegion("user");
		User usr = (User) query.uniqueResult();
		
		
		if (usr == null) {
			log.error(username+" | User "+username+" does not exist");
			throw new InvalidStateException(new RestError(2400, "User "+username+" does not exist"));
		}
		
		if (password != null) {
			try {
				usr.setPassword_hash(PasswordHash.createHash(password));
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				log.error(username+" | Unable to create password hash");
				e.printStackTrace();
				throw new RuntimeException("Unable to create password hash");
			}
		}
		
		if (email != null && !email.equals(usr.getEmail())) {
			// Check that an existing user doesn't have the same email
			Query emailQuery = session.createQuery("select count(*) from User where email = :email");
			emailQuery.setParameter("email", email);
			
			Integer usersWithSameEmail = ( (Long) emailQuery.iterate().next() ).intValue();
		
			if (usersWithSameEmail > 0) {
				log.error(username+" | Email address "+email+" is already registered");
				throw new InvalidStateException(new RestError(2401, "Email address "+email+" is already registered"));
			} else {
				usr.setEmail(email);
			}
		}
		
		if (timezone != null) {
			usr.setTimezone(timezone);
		}
		
		if (deviceID != null) {
			usr.setDeviceID(deviceID);
		}
		
		if (pushActive != null) {
			usr.setPushActive(pushActive);
		}
		
		if (newUsername != null) {
			// Check that a different username has been supplied
			if (newUsername.equals(username)) {
				log.error(username+" | New username provided is same as your current username!");
				throw new InvalidParameterException(new RestError(2404, "New username provided is same as your current username!"));
			}
			
			// Check if username is a banned word 
			if (IPUtil.isBadWord(newUsername)) {
				log.debug(username+" | Username "+newUsername+" is not available");
				throw new InvalidStateException(new RestError(2402, "Username "+newUsername+" is not available"));
			}	
			
			// Check if username is available
			Query usrQuery = session.createQuery("select count(*) from User u WHERE u.username = :newUsername");
			usrQuery.setParameter("newUsername", newUsername);
			Long exists = (Long) usrQuery.uniqueResult();
			if (exists > 0) {
				log.error(username+" | Cannot change username, user "+newUsername+" already exists");
				throw new InvalidStateException(new RestError(2403, "Username "+newUsername+" is not available"));
			} else {
				log.debug(username+" | Changing username to "+newUsername);
				usr.setUsername(newUsername);
			}
			
		}
		
		
		// Check if new game center login provided
		if (gcID != null && !gcID.equals(usr.getGamecenter_id())) {
			// Check if someone is already registered with that gamecenter_id
			Query gcQuery = session.createQuery("select 1 from User u WHERE u.gamecenter_id = :gcID");
			gcQuery.setParameter("gcID", gcID);
			if (gcQuery.uniqueResult() != null) {
				throw new InvalidParameterException(
						new RestError(2404, "User with gamecenter_id "+gcID+" is already registered")
				);
			} 
			
			usr.setGamecenter_id(gcID);
		}
		
		if (gcUsername != null) {
			usr.setGamecenter_username(gcUsername);
		}
		
		
		// Check if new facebook login provided
		if (fbID != null && !fbID.equals(usr.getFacebook_id())) {
			// Check if user already exists with this facebook ID
			Query fbQuery = session.createQuery("select 1 from User u WHERE u.facebook_id = :fbID");
			fbQuery.setParameter("fbID", fbID);
			if (fbQuery.uniqueResult() != null) {
				throw new InvalidParameterException(
						new RestError(2405, "User with facebook_id "+fbID+" is already registered")
				);
			} else {
				usr.setFacebook_id(fbID);
			}
		}
		
		if (fbUsername != null) {
			usr.setFacebook_username(fbUsername);
		}
		
		if (fbEmail != null) {
			usr.setFacebook_email(fbEmail);
			if (usr.getEmail() == null) {
				usr.setEmail(fbEmail);
			}
		}
		
		if (fbFullName != null) {
			usr.setFacebook_full_name(fbFullName);
		}		
		
		session.update(usr);
		
		// Return user on success
		return usr;
	}
	
	
	/*
	 * Authenticate a user based on their password
	 */
	public boolean authenticate(String username, String pass) {
				
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Get details of the requested user
		Query query = session.createQuery("FROM User u WHERE u.username = :username");
		query.setParameter("username", username);
		query.setCacheable(true);
		query.setCacheRegion("user");
		User usr = (User) query.uniqueResult();
		
		boolean isValid = false;
		
		try {
			isValid = PasswordHash.validatePassword(pass, usr.getPassword_hash());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		return isValid;
	}
	
	
	/*
	 * GET user/account
	 */
	public User getUser(String username, String gcID, String fbID) {
		
		log.debug(username+" | Getting user account");
		
	    // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		User usr = null;
		
		if (username != null) {
			Query query = session.createQuery("FROM User u WHERE u.username = :username");
			query.setParameter("username", username);
			query.setCacheable(true);
			query.setCacheRegion("user");
			usr = (User) query.uniqueResult();
		} else if (gcID != null) {
			Query query = session.createQuery("FROM User u WHERE u.gamecenter_id = :gcID");
			query.setParameter("gcID", gcID);
			query.setCacheable(true);
			query.setCacheRegion("user");
			usr = (User) query.uniqueResult();
		} else if (fbID != null) {
			Query query = session.createQuery("FROM User u WHERE u.facebook_id = :fbID");
			query.setParameter("fbID", fbID);
			query.setCacheable(true);
			query.setCacheRegion("user");
			usr = (User) query.uniqueResult();
		}
		
		return usr;
	}


	/*
	 * POST user/fan
	 */
	public Integer setUserFan(Integer comp_id, Integer fangroup_id, String username) {

		log.debug(username+" | Selecting fangroup "+fangroup_id+" for competition "+comp_id);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		FanGroup fangroup;
		try {
			fangroup = (FanGroup) session.get(FanGroup.class, fangroup_id);
		}
		catch(Exception e) {
			log.error(username+" | Fangroup "+fangroup_id+" does not exist");
			throw new InvalidStateException(new RestError(2103, "Fangroup "+fangroup_id+" does not exist"));
		}
		
		if (fangroup == null || fangroup.getComp_id() != comp_id) {
			log.error(username+" | Fangroup "+fangroup_id+" does not exist in competition "+comp_id);
			throw new InvalidStateException(new RestError(2102, "Fangroup "+fangroup_id+" does not exist in competition "+comp_id));
		}
		
		
		// Check to see if the user already has a fangroup selected for this competition
		StringBuffer queryString = new StringBuffer("from Fan f where f.user.username = :username ");
		queryString.append("and f.fangroup.competition.comp_id = :comp_id");
		
		Query query = session.createQuery(queryString.toString());
		query.setParameter("username", username);
		query.setParameter("comp_id", comp_id);
		
		@SuppressWarnings("unchecked")
		List<Fan> result = query.list();
		
		if (result.isEmpty()) {
			log.debug(username+" | User has not yet selected fangroup for competition "+comp_id+", therefore making new selection");
			
			// Get user's record
			Query usrQuery = session.createQuery("FROM User u WHERE u.username = :username");
			usrQuery.setParameter("username", username);
			usrQuery.setCacheable(true);
			usrQuery.setCacheRegion("user");
			User usr = (User) usrQuery.uniqueResult();

			// User has not yet selected fangroup for this competition, 
			// so we can set the fangroup			
			Fan f = new Fan();
			
			f.setFangroup(fangroup);
			f.setUser(usr);
			f.setUser_id(usr.getUser_id());

			session.save(f);
			
			// For alpha we are just returning null on success
			return null;

		} else {
			Fan currentFan = result.get(0);
			if (currentFan.getFangroup_id() == fangroup_id) {
				log.error(username+" | User has already selected fangroup "+fangroup_id+" for competition "+comp_id);
				throw new InvalidStateException(new RestError(2100, "You have already selected this fangroup for this competition"));
			} else {
				// Do not allow user to change fangroup if they already have 
				// a game entry in this competition
				StringBuffer gameEntryQueryString = new StringBuffer("from GameEntry ge where ge.game.competition_id = :comp_id ");
				gameEntryQueryString.append("and ge.user.username = :username");
				
				Query gameEntryQuery = session.createQuery(gameEntryQueryString.toString());
				gameEntryQuery.setParameter("username", username);
				gameEntryQuery.setParameter("comp_id", comp_id);
				
				int numEntriesInComp = gameEntryQuery.list().size();
				if (numEntriesInComp > 0) {
					log.error(username+" | Unable to set a new fangroup, user has already played a game in competition "+comp_id);
					throw new InvalidStateException(new RestError(2101, "Unable to set new fangroup, you have already played a game in this competition"));
				}
							
				// Set fangroup as user has not yet entered any games in this competition
				currentFan.setFangroup((FanGroup) session.load(FanGroup.class, fangroup_id));
				session.update(currentFan);
				log.debug(username+" | Fangroup selection updated to "+fangroup_id+" for competition "+comp_id);
				return null;
			}
			
		}

	}


	/*
	 * GET user/fangroups
	 */
	@SuppressWarnings("unchecked")
	public List <FanGroup> getUserFangroups(String username) {
		
		log.debug(username+" | Getting users fangroups");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();

		// Check to see if the user already has a fangroup selected for this competition
		StringBuffer queryString = new StringBuffer("select fangroup from Fan f ");
		queryString.append("where f.user.username = :username");
		
		Query query = session.createQuery(queryString.toString());
		query.setParameter("username", username);
		
		return query.list();
	}

	
	/*
	 * GET user/stats
	 */
	public UserStatsResponse getUserStats(String username) {
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(authed_user+" | Getting user stats for user: "+username);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		StringBuffer queryString = new StringBuffer("select ");
		queryString.append("us.user.username, ");
		queryString.append("us.total_winnings, ");
		queryString.append("us.total_rank, ");
		queryString.append("us.total_games_played, ");
		queryString.append("us.total_pc_correct, ");
		queryString.append("us.total_user_rating, ");
		queryString.append("us.global_winnings, ");
		queryString.append("us.fangroup_winnings, ");
		queryString.append("us.h2h_winnings, ");
		queryString.append("us.global_games_won, ");
		queryString.append("us.fangroup_pools_won, ");
		queryString.append("us.h2h_won, ");
		queryString.append("us.h2h_pc_correct, ");	
		queryString.append("(select count(*) as num_users_in_system from User) ");
		queryString.append("from UserStats us ");
		//queryString.append("left join (select count(*) as num_users_in_system from User) as num_users");
		queryString.append("where us.user.username = :username");
		
		Query query = session.createQuery(queryString.toString());
		query.setParameter("username", username);

		UserStatsResponse response = null;
		
		@SuppressWarnings("rawtypes")
		Iterator userStats = query.list().iterator();
		while(userStats.hasNext()) {

			response = new UserStatsResponse();
			
			Object[] row = (Object[]) userStats.next();
			String user = (String) row[0];
			Integer total_winnings = (Integer) row[1];
			Integer total_rank = (Integer) row[2];
			Integer total_games_played = (Integer) row[3];
			Double total_pc_correct = (Double) row[4];
			String total_user_rating = (String) row[5];
			Integer global_winnings = (Integer) row[6];
			Integer fangroup_winnings = (Integer) row[7];
			Integer h2h_winnings = (Integer) row[8];
			Integer global_games_won = (Integer) row[9];			
			Integer fangroup_pools_won = (Integer) row[10];
			Integer h2h_won = (Integer) row[11];
			Double h2h_pc_correct = (Double) row[12];
			Integer num_users_in_system = ((Number) row[13]).intValue();
			
			response.setUsername(user);
			response.setTotal_winnings(total_winnings);
			response.setTotal_rank(total_rank);
			response.setTotal_games_played(total_games_played);
			response.setTotal_pc_correct(total_pc_correct);
			response.setTotal_user_rating(total_user_rating);
			response.setGlobal_winnings(global_winnings);
			response.setFangroup_winnings(fangroup_winnings);
			response.setH2h_winnings(h2h_winnings);
			response.setGlobal_games_won(global_games_won);
			response.setFangroup_pools_won(fangroup_pools_won);
			response.setH2h_won(h2h_won);
			response.setH2h_pc_correct(h2h_pc_correct);
			response.setNum_users_in_system(num_users_in_system);
		}

		if (response != null) {
			return response;
		} else {
			log.error(authed_user+" | No stats found for user "+username);
			throw new InvalidStateException(new RestError(2700, "No stats found for user "+username));
		}
	
	}
	
	
	/*
	 * GET user/leaderboard
	 */
	public List<UserLeaderboardResponse> getUserLeaderboard() {
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(authed_user+" | Getting user leaderboard");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		StringBuffer queryString = new StringBuffer("select ");
		queryString.append("us.total_rank as rank, ");
		queryString.append("us.user.username, ");
		queryString.append("us.total_winnings as winnings ");
		queryString.append("from UserStats us ");
		queryString.append("where us.total_rank > 0 ");
		queryString.append("order by us.total_rank asc ");
		
		Query query = session.createQuery(queryString.toString());
		query.setMaxResults(100);
		
		List <UserLeaderboardResponse> response = new ArrayList<>();
		
		@SuppressWarnings("rawtypes")
		Iterator userLeaders = query.list().iterator();
		while(userLeaders.hasNext()) {
			// Process each competition winner and add to response object
			Object[] row = (Object[]) userLeaders.next();
			Integer rank = (Integer) row[0];
			String username = (String) row[1];
			Integer winnings = (Integer) row[2];
			
			UserLeaderboardResponse ulbr = new UserLeaderboardResponse();
			ulbr.setRank(rank);
			ulbr.setUsername(username);
			ulbr.setWinnings(winnings);
			
			response.add(ulbr);
		}
		
		return response;
		
	}
	
	
	/*
	 * GET user/motd
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMotd() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(username+" | Getting motd");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from Motd m where m.user.username = :username and m.processed = 0 order by m.updated desc");
		query.setParameter("username", username);
		query.setMaxResults(Threshold.MAX_MESSAGES_OF_DAY);
		
		List<String> messages = new ArrayList<>();
		List<Motd> messagesOfDay = query.list();
		
		for (Motd motd: messagesOfDay) {
			messages.add(motd.getMessage());
			motd.setProcessed(1);
			session.save(motd);
		}
		
		if (!messages.isEmpty()) {
			return messages;
		} else {
			return null;
		}
		
	}
	
	
	/*
	 * GET user/list
	 */
	@SuppressWarnings("unchecked")
	public List<String> getUserList(boolean hideBots) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(username+" | Getting list of usernames in system");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		StringBuffer queryString = new StringBuffer("select u.username from User u");
		if (hideBots) {
			queryString.append(" where u.bot = false");
		}
		
		Query query = session.createQuery(queryString.toString());
		query.setMaxResults(Threshold.MAX_USERS_IN_LIST);
				
		return query.list();
	}
	
	
	/*
	 * POST user/pat
	 * TODO: PAT TABLE NO LONGER EXISTS, EITHER DECO CODE OR RECREATE PAT TABLE
	 */
	@SuppressWarnings("unchecked")
	public User pat(String fromUser, String toUser, Integer comp_id, Integer game_id) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Must either specify comp_id or game_id
		if (comp_id == null && game_id == null) {
			throw new InvalidParameterException(new RestError(2500, "Must specify either comp_id or game_id when patting a user"));
		}
		
		// Cannot specify both comp_id and game_id 
		// (Don't want to have to check whether the given game_id is for that competition)
		if (comp_id != null && game_id != null) {
			throw new InvalidParameterException(new RestError(2501, "Must specify either comp_id or game_id when patting a user, not both"));
		}
		
		// Cannot pat yourself
		if (fromUser.equals(toUser)) {
			throw new InvalidParameterException(new RestError(2503, "You cannot pat yourself!"));
		}
		
		// Verify whether fromUser is eligible to pat toUser at this time
		StringBuffer queryString = new StringBuffer("from Pat p where p.fromUser = '");
		queryString.append(fromUser).append("' and p.toUser = '").append(toUser);
		
		if (comp_id != null) {
			queryString.append("' and p.competition = ").append(comp_id);
		}
		else {
			Game g = (Game) session.get(Game.class, game_id);
			queryString.append("' and p.competition = ").append(g.getCompetition_id());
		}

		queryString.append(" ORDER BY created desc");
		
		Query query = session.createQuery(queryString.toString()).setMaxResults(1);
		
		List <Pat> result = query.list();

		// Check that fromUser hasn't recently patted toUser in this competition 
		if (!result.isEmpty()) {	
			Pat p = result.get(0);
			if (p.getCreated().toDateTime().isAfter(DateTime.now(DateTimeZone.UTC).minusHours(3))) {
				throw new InvalidStateException(new RestError(2502, "You have recently patted this user in this competition, please try again later!"));
			}
		}
		
				
		// Pat the user
		Pat newPat = new Pat();
		newPat.setCompetition((Competition) session.load(Competition.class, comp_id));
		newPat.setFromUser((User) session.load(User.class, fromUser));
		newPat.setToUser((User) session.load(User.class, toUser));
		session.save(newPat);

		// Increment total pat count for user being patted
		StringBuffer updateQueryString = new StringBuffer("update User u set u.total_pat_count = u.total_pat_count + 1 ");
		updateQueryString.append("where u.username = '").append(toUser).append("'");
		Query updateQuery = session.createQuery(updateQueryString.toString());
		updateQuery.executeUpdate();
		
		return null; 
	}
	
	
	/*
	 * Invite user to pool
	 */
	public void inviteUser(User sourceUser, String facebookId, String email, Pool pool) {
		
		StringBuffer logMsg = new StringBuffer("Inviting user to inplayrs:");
		if (facebookId != null) {
			logMsg.append(" facebook_id="+facebookId);
		}
		if (email != null) {
			logMsg.append(" email="+email);
		}
		if (pool != null) {
			logMsg.append(" pool="+pool.getPool_id());
		}
		
		log.debug(sourceUser.getUsername()+" | "+logMsg.toString());
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		UserInvite invite = new UserInvite();
		invite.setSourceUser(sourceUser);
		invite.setFacebookId(facebookId);
		invite.setEmail(email);
		invite.setPool(pool);
		
		session.save(invite);	
	}
	
	
}
