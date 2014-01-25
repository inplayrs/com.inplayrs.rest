package com.inplayrs.rest.service;

import java.util.ArrayList;
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

import com.inplayrs.rest.constants.Result;
import com.inplayrs.rest.ds.Pool;
import com.inplayrs.rest.ds.PoolMember;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.InvalidStateException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.requestds.UserList;
import com.inplayrs.rest.responseds.MyPoolResponse;
import com.inplayrs.rest.responseds.PoolMemberResponse;
import com.inplayrs.rest.util.IPUtil;

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
	public Pool createPool(String name, UserList userList) {
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug(username+" | Creating pool "+name);
		
		// Check pool name for bad words
		String[] poolNameWords = name.split("\\s+|[_.,+]");
		for (String word: poolNameWords) {
			if (IPUtil.isBadWord(word)) {
				throw new InvalidStateException(new RestError(2803, "Pool '"+name+"' not available, please try a different name"));
			}
		}
		
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
				throw new InvalidStateException(new RestError(2800, "Pool '"+name+"' not available, please try a different name"));
			}
		} else {
			log.error(username+" | Pool "+name+" already exists");
			throw new InvalidStateException(new RestError(2800, "Pool '"+name+"' not available, please try a different name"));
		}
		
		// Add pool creator to pool
		log.debug(username+" | Adding creator of pool as pool member");
		int addUserResult = _addPoolMember(pool, username, null);
		if (addUserResult != Result.SUCCESS) {
			throw new InvalidStateException(new RestError(2802, "Failed to add creator of pool as a pool member"));
		}
		
		// Add list of users to pool
		if (userList != null) {
			log.debug(username+" | Adding users to pool "+name);
			
			boolean allPoolMembersAddedSuccessfully = addPoolMembers(pool.getPool_id(), userList);
			if (!allPoolMembersAddedSuccessfully) {
				throw new InvalidStateException(new RestError(2803, "Failed to add all specified pool members to pool"));
			}
		}
		
		// return pool object
		return pool;
		
	}
	
	
	/*
	 * POST pool/addusers
	 */
	public Boolean addPoolMembers(Integer pool_id, UserList userList) {
		// Get username of player
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		
		log.debug(authed_user+" | Adding pool members to pool "+pool_id);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		Pool pool = (Pool) session.get(Pool.class, pool_id);
		if (pool == null) {
			log.error(authed_user+" | Pool with ID "+pool_id+" does not exist");
			throw new InvalidParameterException(new RestError(2900, "Pool with ID "+pool_id+" does not exist"));
		}
		
		List<String> addedUsernames = new ArrayList<>();
		List<String> nonExistantUsernames = new ArrayList<>();
		List<String> usernamesAlreadyInPool = new ArrayList<>();
		List<String> addedFBIDs = new ArrayList<>();
		List<String> nonExistantFBIDs = new ArrayList<>();
		List<String> fbIDsAlreadyInPool = new ArrayList<>();
		
		// Process usernames
		if (!userList.getUsernames().isEmpty()) {
			for (String username : userList.getUsernames()) {
				int userAdded = _addPoolMember(pool, username, null);
				if (userAdded == Result.SUCCESS) {
					addedUsernames.add(username);
				} else if (userAdded == Result.USER_DOES_NOT_EXIST){
					nonExistantUsernames.add(username);
				} else if (userAdded == Result.USER_ALREADY_IN_POOL) {
					usernamesAlreadyInPool.add(username);
				}
			}
		}
		
		// Process facebookIDs
		if (!userList.getFacebookIDs().isEmpty()) {
			for (String fbID : userList.getFacebookIDs()) {
				int userAdded = _addPoolMember(pool, null, fbID);
				if (userAdded == Result.SUCCESS) {
					addedFBIDs.add(fbID);
				} else if (userAdded == Result.USER_DOES_NOT_EXIST){
					nonExistantFBIDs.add(fbID);
				} else if (userAdded == Result.USER_ALREADY_IN_POOL) {
					fbIDsAlreadyInPool.add(fbID);
				}
			}
		}	
			
		// Check which users failed to be added to pool
		StringBuffer errorMsg = new StringBuffer("Could not add all users to pool ").append(pool_id);
		
		if (!nonExistantUsernames.isEmpty()) {
			errorMsg.append(". The following usernames do not exist: "+IPUtil.listToCommaSeparatedString(nonExistantUsernames));
		}
		
		if (!usernamesAlreadyInPool.isEmpty()) {
			errorMsg.append(". The following users are already in the pool: "+IPUtil.listToCommaSeparatedString(usernamesAlreadyInPool));
		}
		
		if (!nonExistantFBIDs.isEmpty()) {
			errorMsg.append(". The following users by facebook_id do not exist: "+IPUtil.listToCommaSeparatedString(nonExistantFBIDs));
		}
		
		if (!fbIDsAlreadyInPool.isEmpty()) {
			errorMsg.append(". The following users by facebook_id are already in the pool: "+IPUtil.listToCommaSeparatedString(fbIDsAlreadyInPool));
		}
		
		// Return success / failure
		if (!nonExistantUsernames.isEmpty() || !usernamesAlreadyInPool.isEmpty() || 
			!nonExistantFBIDs.isEmpty() || !fbIDsAlreadyInPool.isEmpty()) {
			throw new InvalidStateException(new RestError(2901, errorMsg.toString()));
		} else {
			return true;
		}

	}
	
	// Helper method to add an individual pool member
	public int _addPoolMember(Pool pool, String username, String fbID) {
		
		// Get username of player
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		User user = null;
		if (username != null) {
			Query userQuery = session.createQuery("FROM User u WHERE u.username = :username");
			userQuery.setParameter("username", username);
			userQuery.setCacheable(true);
			userQuery.setCacheRegion("user");
			user = (User) userQuery.uniqueResult();
			if (user == null) {
				log.error(authed_user+" | User with username "+username+" does not exist, cannot add to pool: "+pool.getName());
				return Result.USER_DOES_NOT_EXIST;
			}
		}
		else if(fbID != null) {
			Query userQuery = session.createQuery("FROM User u WHERE u.facebook_id = :fbID");
			userQuery.setParameter("fbID", fbID);
			userQuery.setCacheable(true);
			userQuery.setCacheRegion("user");
			user = (User) userQuery.uniqueResult();
			if (user == null) {
				log.error(authed_user+" | User with facebook_id "+fbID+" does not exist, cannot add to pool: "+pool.getName());
				return Result.USER_DOES_NOT_EXIST;
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
			return Result.USER_ALREADY_IN_POOL;
		}
		
		// Create new pool member
		try {
			session.save(poolMember);
		} catch (ConstraintViolationException e) {
			log.error(authed_user+" | User "+user.getUsername()+" is already a member of pool: "+pool.getName()+". Attempted insert but got ConstraintViolationException");
			return Result.USER_ALREADY_IN_POOL;
		}
		
		log.debug(authed_user+" | Successfully added user "+user.getUsername()+" to pool "+pool.getName());
		
		return Result.SUCCESS;
		
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
		
		Query query = session.createQuery("select pm.user.username as username, pm.user.facebook_id as facebook_id "+
										  "from PoolMember pm where pm.pool.pool_id = :pool_id");
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
