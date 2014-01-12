package com.inplayrs.rest.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.ds.Pool;
import com.inplayrs.rest.ds.PoolMember;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.InvalidStateException;
import com.inplayrs.rest.exception.RestError;

@Service("poolService")
@Transactional
public class PoolService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	//get log4j handler
	private static final Logger log = Logger.getLogger("APILog");
	
	/*
	 * POST pool/create
	 */
	public Pool createPool(String name, String userList) {
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(username+" | Creating pool "+name);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		// Get user object for user creating the pool
		Query userQuery = session.createQuery("FROM User u WHERE u.username = :username");
		userQuery.setParameter("username", username);
		userQuery.setCacheable(true);
		userQuery.setCacheRegion("user");
		User user = (User) userQuery.uniqueResult();
		
		// See if pool already exists
		Query query = session.createQuery("FROM Pool p WHERE p.name = :name");
		query.setParameter("name", name);
		query.setCacheable(true);
		query.setCacheRegion("pool");
		Pool pool = (Pool) query.uniqueResult();
		
		if (pool == null) {
			// Create pool object and save
			pool = new Pool();
			pool.setName(name);
			pool.setNum_players(0);
			pool.setCreated_by(user);
			
			try {
				session.save(pool);
			}
			catch (ConstraintViolationException e) {
				log.error(username+" | Pool "+name+" already exists. Attempted insert but got ConstraintViolationException");
				throw new InvalidStateException(new RestError(2800, "Pool '"+name+"' already exists, please try a different name"));
			}
		} else {
			log.error(username+" | Pool "+name+" already exists");
			throw new InvalidStateException(new RestError(2800, "Pool '"+name+"' already exists, please try a different name"));
		}
		
		if (userList != null) {
			// TODO - Add users to pool
			log.debug(username+" | Adding users to pool "+name);
			
			
			
			
		}
		
		// return pool object
		return pool;
		
	}
	
	
	public Boolean addPoolMember(Integer pool_id, String username, String fbID) {
		
		// Get username of player
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		Pool pool = (Pool) session.get(Pool.class, pool_id);
		if (pool == null) {
			log.error(authed_user+" | Pool with ID "+pool_id+" does not exist");
			throw new InvalidParameterException(new RestError(2902, "Pool with ID "+pool_id+" does not exist"));
		}
		
		User user = null;
		if (username != null) {
			Query userQuery = session.createQuery("FROM User u WHERE u.username = :username");
			userQuery.setParameter("username", username);
			userQuery.setCacheable(true);
			userQuery.setCacheRegion("user");
			user = (User) userQuery.uniqueResult();
			if (user == null) {
				log.error(authed_user+" | User with username "+username+" does not exist");
				throw new InvalidParameterException(new RestError(2903, "User with username "+username+" does not exist"));
			}
		}
		else if(fbID != null) {
			Query userQuery = session.createQuery("FROM User u WHERE u.fbID = :fbID");
			userQuery.setParameter("fbID", fbID);
			userQuery.setCacheable(true);
			userQuery.setCacheRegion("user");
			user = (User) userQuery.uniqueResult();
			if (user == null) {
				log.error(authed_user+" | User with fbID "+fbID+" does not exist");
				throw new InvalidParameterException(new RestError(2904, "User with fbID "+fbID+" does not exist"));
			}
		}
		
		PoolMember poolMember = new PoolMember();
		poolMember.setPool(pool);
		poolMember.setUser(user);
		
		// See if pool member already exists
		Query poolMemberQuery = session.createQuery("select 1 from PoolMember pm where pm.pool.pool_id = :pool_id and pm.user.user_id = :user_id");
		poolMemberQuery.setParameter("pool_id", pool.getPool_id());
		poolMemberQuery.setParameter("user_id", user.getUser_id());
		if (poolMemberQuery.uniqueResult() != null) {
			log.error(authed_user+" | User "+user.getUsername()+" is already a member of pool: "+pool.getName());
			throw new InvalidStateException(new RestError(2905, "User "+user.getUsername()+" is already a member of pool: "+pool.getName()));
		}
		
		// Create new pool member
		try {
			session.save(poolMember);
		} catch (ConstraintViolationException e) {
			log.error(authed_user+" | User "+user.getUsername()+" is already a member of pool: "+pool.getName()+". Attempted insert but got ConstraintViolationException");
			throw new InvalidStateException(new RestError(2906, "User "+user.getUsername()+" is already a member of pool: "+pool.getName()));
		}
		
		log.debug(authed_user+" | Successfully added user "+user.getUsername()+" to pool "+pool.getName());
		
		return true;
		
	}
	
	
}
