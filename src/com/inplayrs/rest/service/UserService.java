package com.inplayrs.rest.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
	
	
	/*
	 * POST user/register
	 */
	public User registerUser(String username, String password, String email) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
				
		// See if user already exists
		User usr = (User) session.get(User.class, username);
		
		if (usr == null) {
			usr = new User();
			usr.setUsername(username);
			try {
				usr.setPassword_hash(PasswordHash.createHash(password));
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to create password hash");
			}
			if (email != null) {
				// Check that an existing user doesn't have the same email
				StringBuffer queryString = new StringBuffer("select count(*) from User where email = '");
				queryString.append(email).append("'");
				
				Integer usersWithSameEmail = ( (Long) session.createQuery(queryString.toString()).iterate().next() ).intValue();
				
				if (usersWithSameEmail > 0) {
					throw new InvalidStateException(new RestError(2301, "Email address "+email+" is already registered"));
				} else {
					usr.setEmail(email);
				}
			}
			
			session.save(usr);
			return usr;
			
		} else {
			throw new InvalidStateException(new RestError(2300, "Username "+username+" is already taken"));
		}
		
	}
	
	
	/*
	 * POST user/account/update
	 */
	public User updateAccount(String username, String password, String email) {

		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();

		// Get details of the user
		User usr = (User) session.get(User.class, username);
		
		if (usr == null) {
			throw new InvalidStateException(new RestError(2400, "User "+username+" does not exist"));
		}
		
		if (password != null) {
			try {
				usr.setPassword_hash(PasswordHash.createHash(password));
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
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
				throw new InvalidStateException(new RestError(2401, "Email address "+email+" is already registered"));
			} else {
				usr.setEmail(email);
			}
		}
		
		session.update(usr);
		return usr;
	}
	
	
	/*
	 * Authenticate a user based on their password
	 */
	public boolean authenticate(String user, String pass) {
				
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Get details of the requested user
		User usr = (User) session.load(User.class, user);
		
		boolean isValid = false;
		
		try {
			isValid = PasswordHash.validatePassword(pass, usr.getPassword_hash());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isValid;
	}
	
	
	/*
	 * GET user/account
	 */
	public User getUser(String username) {
	    // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Retrieve existing user first
		User user = (User) session.get(User.class, username);
		   
		return user;
	}


	/*
	 * POST user/fan
	 */
	public Integer setUserFan(Integer comp_id, Integer fangroup_id, String username) {

		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		FanGroup fangroup;
		try {
			fangroup = (FanGroup) session.get(FanGroup.class, fangroup_id);
		}
		catch(Exception e) {
			throw new InvalidStateException(new RestError(2103, "Fangroup with ID "+fangroup_id+" does not exist"));
		}
		
		if (fangroup == null || fangroup.getComp_id() != comp_id) {
			throw new InvalidStateException(new RestError(2102, "Fangroup with ID "+fangroup_id+" does not exist in competition with ID "+comp_id));
		}
		
		
		// Check to see if the user already has a fangroup selected for this competition
		StringBuffer queryString = new StringBuffer("from Fan f where f.user = '");
		queryString.append(username);
		queryString.append("' and f.fangroup.competition = ");
		queryString.append(comp_id);
		
		Query query = session.createQuery(queryString.toString());
		
		@SuppressWarnings("unchecked")
		List<Fan> result = query.list();
		
		if (result.isEmpty()) {
			System.out.println("XXX User has not yet selected fangroup for this competition");
			
			// User has not yet selected fangroup for this competition, 
			// so we can set the fangroup			
			Fan f = new Fan();
			
			f.setFangroup(fangroup);
			f.setUser((User) session.load(User.class, username));

			session.save(f);
			
			// For alpha we are just returning null on success
			return null;

		} else {
			Fan currentFan = result.get(0);
			if (currentFan.getFangroup_id() == fangroup_id) {
				throw new InvalidStateException(new RestError(2100, "You have already selected this fangroup for this competition"));
			} else {
				// Do not allow user to change fangroup if they already have 
				// a game entry in this competition
				StringBuffer gameEntryQueryString = new StringBuffer("from GameEntry ge where ge.game.competition_id = ");
				gameEntryQueryString.append(comp_id).append(" and ge.user = '");
				gameEntryQueryString.append(username).append("'");
				
				Query gameEntryQuery = session.createQuery(gameEntryQueryString.toString());
				int numEntriesInComp = gameEntryQuery.list().size();
				if (numEntriesInComp > 0) {
					throw new InvalidStateException(new RestError(2101, "Unable to set new fangroup, you have already played a game in this competition"));
				}
							
				// Set fangroup as user has not yet entered any games in this competition
				currentFan.setFangroup((FanGroup) session.load(FanGroup.class, fangroup_id));
				session.update(currentFan);
				return null;
			}
			
		}

	}


	/*
	 * GET user/fangroups
	 */
	@SuppressWarnings("unchecked")
	public List <FanGroup> getUserFangroups(String username) {
		 // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   

		// Check to see if the user already has a fangroup selected for this competition
		StringBuffer queryString = new StringBuffer("select fangroup from Fan f ");
		queryString.append("where f.user = '");
		queryString.append(username);
		queryString.append("'");
		
		Query query = session.createQuery(queryString.toString());
		
		return query.list();
		
	}

	
	
	/*
	 * POST user/pat
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
