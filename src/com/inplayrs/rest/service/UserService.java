package com.inplayrs.rest.service;

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


/*
 * Service for processing user info
 */
@Service("userService")
@Transactional
public class UserService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	
	public boolean authenticate(String user, String pass) {
		
		if ( (user.equals("chris") && pass.equals("pw1")) ||
			 (user.equals("anil") && pass.equals("pw2")) ||
			 (user.equals("david") && pass.equals("pw3")) ||
			 (user.equals("mani") && pass.equals("pw4")) ) {
			
			return true;
		} else {
			return false;
		}		
	
	}
	
	
	public User getUser(int user_id) {
	    // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Retrieve existing user first
		User user = (User) session.get(User.class, user_id);
		   
		return user;
	}


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
			return f.getFan_id();
		} else {
			Fan currentFan = result.get(0);
			if (currentFan.getFangroup_id() == fangroup_id) {
				System.out.println("User has already selected this fangroup");
				return 0;
			} else {
				// Set fangroup.  In future release we will prevent users from changing
				// their fangroup after the competition has started
				currentFan.setFangroup((FanGroup) session.load(FanGroup.class, fangroup_id));
				session.update(currentFan);
				return currentFan.getFan_id();
			}
			
		}

	}
	
	
	
	
	
}
