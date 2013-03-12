package com.inplayrs.rest.service;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.ds.Period;
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
	
	
	
	
}
