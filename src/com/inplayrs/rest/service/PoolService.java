package com.inplayrs.rest.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.ds.Pool;
import com.inplayrs.rest.ds.PoolMember;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.InvalidStateException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.responseds.MyPoolResponse;
import com.inplayrs.rest.responseds.PoolMemberResponse;

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
	
	
	
	/*
	 * GET pool/mypools
	 */
	@SuppressWarnings("unchecked")
	public List<MyPoolResponse> getMyPools() {
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		log.debug(username+" | Getting list of pools entered");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		Query query = session.createQuery("select pm.pool.pool_id as pool_id, pm.pool.name as name from PoolMember pm where pm.user.username = :username");
		query.setParameter("username", username);
		query.setResultTransformer(Transformers.aliasToBean(MyPoolResponse.class));
		
		List<MyPoolResponse> response = query.list();
		
		if (response.isEmpty()) {
			log.debug(username+" | Not a member of any pools");
			return null;
		}

		return response;
	}
	
	
	/*
	 * GET pool/members
	 */
	@SuppressWarnings("unchecked")
	public List<PoolMemberResponse> getPoolMembers(Integer pool_id) {
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		log.debug(username+" | Getting list of pool members for pool "+pool_id);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		Query query = session.createQuery("select pm.user.user_id as user_id, pm.user.username as username, "+
										  "pm.user.facebook_id as facebook_id from PoolMember pm where pm.pool.pool_id = :pool_id");
		query.setParameter("pool_id", pool_id);
		query.setResultTransformer(Transformers.aliasToBean(PoolMemberResponse.class));
		
		List <PoolMemberResponse> response = query.list();
		
		if (response.isEmpty()) {
			log.debug(username+" | Pool "+pool_id+" contains no members");
			return null;
		}

		return response;
		
	}
	
	
	/*
	 * POST pool/leave
	 */
	public Boolean leavePool(Integer pool_id) {
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		log.debug(username+" | Leaving pool "+pool_id);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		// Verify that user is in pool
		Query query = session.createQuery("from PoolMember pm where pm.user.username = :username and pm.pool.pool_id = :pool_id");
		query.setParameter("username", username);
		query.setParameter("pool_id", pool_id);
		PoolMember poolMember = (PoolMember) query.uniqueResult();
		
		if (poolMember == null) {
			log.error(username+" | Cannot leave pool as user is not a member of pool "+pool_id);
			throw new InvalidStateException(new RestError(3000, "You are not a member of this pool"));
		}
		
		// Remove game entries for user in that pool
		log.debug(username+" | Removing pool game entries for user in pool "+pool_id);
		
		Query removePoolGameEntriesQuery = session.createQuery("delete from PoolGameEntry pge where pge.poolMember in ("+
						"select pm.poolMemberID from PoolMember pm where pm.user.username = :username "+
						"and pm.pool.pool_id = :pool_id)");
		removePoolGameEntriesQuery.setParameter("username", username);
		removePoolGameEntriesQuery.setParameter("pool_id", pool_id);
		int poolGameEntriesDeleted = removePoolGameEntriesQuery.executeUpdate();
		
		log.debug(username+" | Removed "+poolGameEntriesDeleted+" pool game entries for user in pool "+pool_id);
		
		// Remove user from pool
		session.delete(poolMember);
		
		return true;
	}
	
}
