package com.inplayrs.rest.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.ds.Fan;
import com.inplayrs.rest.ds.FanGroup;
import com.inplayrs.rest.ds.User;
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
	 * Registers a user - creates an account
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
				usr.setEmail(email);
			}
			
			session.save(usr);
			return usr;
			
		} else {
			throw new RuntimeException("User already exists");
		}
		
	}
	
	
	/*
	 * Update user's account details - password, email
	 */
	public User updateAccount(String username, String password, String email) {

		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();

		// Get details of the user
		User usr = (User) session.get(User.class, username);
		
		if (password != null) {
			try {
				usr.setPassword_hash(PasswordHash.createHash(password));
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to create password hash");
			}
		}
		
		if (email != null) {
			usr.setEmail(email);
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
	 * Get a user account
	 */
	public User getUser(String username) {
	    // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Retrieve existing user first
		User user = (User) session.get(User.class, username);
		   
		return user;
	}


	/*
	 * Set the fangroup for a user
	 */
	public Integer setUserFan(Integer comp_id, Integer fangroup_id, String username) {

		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Check to see if the user already has a fangroup selected for this competition
		StringBuffer queryString = new StringBuffer("from Fan f where f.user = '");
		queryString.append(username);
		queryString.append("' and f.fangroup.comp_id = ");
		queryString.append(comp_id);
		
		Query query = session.createQuery(queryString.toString());
		
		@SuppressWarnings("unchecked")
		List<Fan> result = query.list();
		
		if (result.isEmpty()) {
			// Set new fan			
			Fan f = new Fan();
			f.setFangroup((FanGroup) session.load(FanGroup.class, fangroup_id));
			f.setUser((User) session.load(User.class, username));
			
			session.save(f);
			
			return null;
			
			// For alpha we are just returning null on success
			//return f.getFan_id();
		} else {
			Fan currentFan = result.get(0);
			if (currentFan.getFangroup_id() == fangroup_id) {
				System.out.println("User has already selected this fangroup");
				return null;
			} else {
				// Set fangroup.  In future release we will prevent users from changing
				// their fangroup after the competition has started
				currentFan.setFangroup((FanGroup) session.load(FanGroup.class, fangroup_id));
				session.update(currentFan);
				
				return null;
				
				// For alpha we are just returning null on success
				//return currentFan.getFan_id();
			}
			
		}

	}


	/*
	 * Get all fangroups for a user
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

	

	
}
