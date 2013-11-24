package com.inplayrs.rest.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

import com.inplayrs.rest.ds.Competition;
import com.inplayrs.rest.ds.Fan;
import com.inplayrs.rest.ds.FanGroup;
import com.inplayrs.rest.ds.Game;
import com.inplayrs.rest.ds.Pat;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.InvalidStateException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.security.PasswordHash;


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
							 String deviceID, Boolean pushActive) {
		
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(authed_user+" | Registering new user: "+username);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
				
		// Verify whether the requested username is in the list of bad words
		String badWordQuery = "select count(*) from BadWord where word = '"+username.toLowerCase()+"'";
		
		if (( (Long) session.createQuery(badWordQuery).iterate().next() ).intValue() > 0) {
			log.debug(authed_user+" | Username "+username+" is not available");
			throw new InvalidStateException(new RestError(2302, "Username "+username+" is not available"));
		}	
		
		// See if user already exists
		Query query = session.createQuery("FROM User u WHERE u.username = '"+username+"'");
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
				StringBuffer queryString = new StringBuffer("select count(*) from User where email = '");
				queryString.append(email).append("'");
				
				Integer usersWithSameEmail = ( (Long) session.createQuery(queryString.toString()).iterate().next() ).intValue();
				
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
		Query query = session.createQuery("select 1 from User u WHERE u.gamecenter_id = '"+gcID+"'");
		if (query.uniqueResult() != null) {
			throw new InvalidParameterException(
					new RestError(2306, "User with gamecenter_id "+gcID+" is already registered")
			);
		}
		
		String username = _getNextAvailableUsername(gcUsername, 0);
		
		// Check if we can use the gamecenter_id as the username
		Query usrQuery = session.createQuery("select 1 from User u WHERE u.username = '"+username+"'");
		
		User usr = new User();
		
		return usr;
	}
	
	
	/*
	 * POST user/register (Facebook registration)
	 */
	public User registerFBUser(String fbID, String fbUsername, String fbEmail, String fbFullName, String password, 
			String email, String timezone, String deviceID, Boolean pushActive) {
		
		User usr = new User();
		
		return usr;
	}
	
	
	public String _getNextAvailableUsername(String username, int attempt) {
		// TODO - code this
		if (attempt > 0) {
			
		} else {
			
		}
		return null;
	}
	
	
	/*
	 * POST user/account/update
	 */
	public User updateAccount(String username, String password, String email, String timezone, String deviceID, Boolean pushActive) {

		log.debug(username+" | Updating user account");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();

		// Get details of the user
		Query query = session.createQuery("FROM User u WHERE u.username = '"+username+"'");
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
			StringBuffer queryString = new StringBuffer("select count(*) from User where email = '");
			queryString.append(email).append("'");;
			
			Integer usersWithSameEmail = ( (Long) session.createQuery(queryString.toString()).iterate().next() ).intValue();
		
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
		
		session.update(usr);
		
		// Return null on success
		return null;
	}
	
	
	/*
	 * Authenticate a user based on their password
	 */
	public boolean authenticate(String user, String pass) {
				
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Get details of the requested user
		Query query = session.createQuery("FROM User u WHERE u.username = '"+user+"'");
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
	public User getUser(String username) {
		
		log.debug(username+" | Getting user account");
		
	    // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Get user's record
		Query query = session.createQuery("FROM User u WHERE u.username = '"+username+"'");
		query.setCacheable(true);
		query.setCacheRegion("user");
		User usr = (User) query.uniqueResult();
		   
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
		StringBuffer queryString = new StringBuffer("from Fan f where f.user.username = '");
		queryString.append(username);
		queryString.append("' and f.fangroup.competition = ");
		queryString.append(comp_id);
		
		Query query = session.createQuery(queryString.toString());
		
		@SuppressWarnings("unchecked")
		List<Fan> result = query.list();
		
		if (result.isEmpty()) {
			log.debug(username+" | User has not yet selected fangroup for competition "+comp_id+", therefore making new selection");
			
			
			// Get user's record
			Query usrQuery = session.createQuery("FROM User u WHERE u.username = '"+username+"'");
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
				StringBuffer gameEntryQueryString = new StringBuffer("from GameEntry ge where ge.game.competition_id = ");
				gameEntryQueryString.append(comp_id).append(" and ge.user.username = '");
				gameEntryQueryString.append(username).append("'");
				
				Query gameEntryQuery = session.createQuery(gameEntryQueryString.toString());
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
		queryString.append("where f.user.username = '");
		queryString.append(username);
		queryString.append("'");
		
		Query query = session.createQuery(queryString.toString());
		
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
	
	
	
	
}
