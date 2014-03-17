package com.inplayrs.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.constants.Result;
import com.inplayrs.rest.constants.Threshold;
import com.inplayrs.rest.ds.Motd;
import com.inplayrs.rest.ds.Pool;
import com.inplayrs.rest.ds.PoolCompLeaderboard;
import com.inplayrs.rest.ds.PoolGameLeaderboard;
import com.inplayrs.rest.ds.PoolMember;
import com.inplayrs.rest.ds.User;
import com.inplayrs.rest.exception.InvalidParameterException;
import com.inplayrs.rest.exception.InvalidStateException;
import com.inplayrs.rest.exception.RestError;
import com.inplayrs.rest.requestds.UserList;
import com.inplayrs.rest.responseds.MyPoolResponse;
import com.inplayrs.rest.responseds.PoolMemberResponse;
import com.inplayrs.rest.responseds.PoolPointsResponse;
import com.inplayrs.rest.util.IPUtil;

@Service("poolService")
@Transactional
public class PoolService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Autowired
	@Resource(name="userService")
	private UserService userService;
	
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
		
		// Check if user is already in the max number of pools allowed
		Query numPoolsQuery = session.createQuery("select count(*) from PoolMember pm where pm.user.username = :username");
		numPoolsQuery.setParameter("username", user.getUsername());
		Integer numPoolsUserIn = ( (Long) numPoolsQuery.iterate().next() ).intValue();
		
		if (numPoolsUserIn >= Threshold.MAX_POOLS_USER_CAN_BE_IN) {
			log.info(username+" | Cannot create pool, user is already in the max number of pools ("+Threshold.MAX_POOLS_USER_CAN_BE_IN+")");
			throw new InvalidStateException(new RestError(2804, "You are already in the max number of pools, please leave a pool before creating a new one"));
		}		
		
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
		int addUserResult = _addPoolMember(pool, username, null, null);
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
		
		// Get user's record
		Query usrQuery = session.createQuery("FROM User u WHERE u.username = :username");
		usrQuery.setParameter("username", authed_user);
		usrQuery.setCacheable(true);
		usrQuery.setCacheRegion("user");
		User user = (User) usrQuery.uniqueResult();
		
		List<String> addedUsernames = new ArrayList<>();
		List<String> nonExistantUsernames = new ArrayList<>();
		List<String> usernamesAlreadyInPool = new ArrayList<>();
		List<String> addedFBIDs = new ArrayList<>();
		List<String> nonExistantFBIDs = new ArrayList<>();
		List<String> fbIDsAlreadyInPool = new ArrayList<>();
		List<String> usernamesInMaxNumPools = new ArrayList<>();
		List<String> fbIDsInMaxNumPools = new ArrayList<>();
		
		// Process usernames
		if (!userList.getUsernames().isEmpty()) {
			for (String username : userList.getUsernames()) {
				int userAdded = _addPoolMember(pool, username, null, null);
				if (userAdded == Result.SUCCESS) {
					addedUsernames.add(username);
				} else if (userAdded == Result.USER_DOES_NOT_EXIST){
					nonExistantUsernames.add(username);
				} else if (userAdded == Result.USER_ALREADY_IN_POOL) {
					usernamesAlreadyInPool.add(username);
				} else if (userAdded == Result.USER_IN_MAX_NUMBER_OF_POOLS) {
					usernamesInMaxNumPools.add(username);
				}
			}
		}
		
		// Process facebookIDs
		if (!userList.getFacebookIDs().isEmpty()) {
			for (String fbID : userList.getFacebookIDs()) {
				int userAdded = _addPoolMember(pool, null, fbID, null);
				if (userAdded == Result.SUCCESS) {
					addedFBIDs.add(fbID);
				} else if (userAdded == Result.USER_DOES_NOT_EXIST){
					nonExistantFBIDs.add(fbID);
				} else if (userAdded == Result.USER_ALREADY_IN_POOL) {
					fbIDsAlreadyInPool.add(fbID);
				} else if (userAdded == Result.USER_IN_MAX_NUMBER_OF_POOLS) {
					fbIDsInMaxNumPools.add(fbID);
				}
			}
		}	
			
		// Check which users failed to be added to pool
		
		String initialErrorMsgString = "Could not add all users to pool "+pool_id;
		StringBuffer errorMsg = new StringBuffer(initialErrorMsgString);
		
		if (!nonExistantUsernames.isEmpty()) {
			errorMsg.append(". The following usernames do not exist: "+IPUtil.listToCommaSeparatedString(nonExistantUsernames));
		}
		
		if (!usernamesAlreadyInPool.isEmpty()) {
			errorMsg.append(". The following users are already in the pool: "+IPUtil.listToCommaSeparatedString(usernamesAlreadyInPool));
		}
		
		if (!usernamesInMaxNumPools.isEmpty()) {
			errorMsg.append(". The following users are already in the max number of pools: "+IPUtil.listToCommaSeparatedString(usernamesInMaxNumPools));
		}
		
		if (!nonExistantFBIDs.isEmpty()) {
			errorMsg.append(". The following users by facebook_id do not exist - inviting them to inplayrs: "+IPUtil.listToCommaSeparatedString(nonExistantFBIDs));
			for (String fbID : nonExistantFBIDs) {
				userService.inviteUser(user, fbID, null, pool);
			}
		}
		
		if (!fbIDsAlreadyInPool.isEmpty()) {
			errorMsg.append(". The following users by facebook_id are already in the pool: "+IPUtil.listToCommaSeparatedString(fbIDsAlreadyInPool));
		}
		
		if (!fbIDsInMaxNumPools.isEmpty()) {
			errorMsg.append(". The following users by facebook_id are already in the max number of pools: "+IPUtil.listToCommaSeparatedString(fbIDsInMaxNumPools));
		}
		
		
		// Log any errors
		if (!initialErrorMsgString.equals(errorMsg.toString())) {
			log.info(errorMsg.toString());
		} 
		
		// Return null as client will just be looking at the HTTP response
		return null;

	}
	
	
	// Helper method to add an individual pool member
	public int _addPoolMember(Pool pool, String username, String fbID, String invitedByUser) {
		
		// Get username of player adding user to pool
		String authed_user = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// Check whether pool has max number of participants in or not
		if (pool.getNum_players() >= Threshold.MAX_POOL_SIZE) {
			log.error(authed_user+" | Cannot add new member to pool '"+pool.getName()+
								     "'. Max pool size of "+Threshold.MAX_POOL_SIZE+" reached");
			throw new InvalidStateException(new RestError(2902, "Cannot add new member to pool '"+pool.getName()+
								     "'. Max pool size of "+Threshold.MAX_POOL_SIZE+" reached"));
		}
			
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		// Get user object
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
		
		// Check if user is already in the max number of pools allowed
		Query numPoolsQuery = session.createQuery("select count(*) from PoolMember pm where pm.user.username = :username");
		numPoolsQuery.setParameter("username", user.getUsername());
		Integer numPoolsUserIn = ( (Long) numPoolsQuery.iterate().next() ).intValue();
		
		if (numPoolsUserIn >= Threshold.MAX_POOLS_USER_CAN_BE_IN) {
			log.info(authed_user+" | User "+user.getUsername()+" is already in max number of pools ("+Threshold.MAX_POOLS_USER_CAN_BE_IN+")");
			return Result.USER_IN_MAX_NUMBER_OF_POOLS;
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
		
		log.debug(authed_user+" | Successfully added user "+user.getUsername()+" to pool "+pool.getName()+". Adding motd to indicate this.");
		
		// Add message to tell user they have been added to pool (unless adding pool creator to pool)
		if (!pool.getCreated_by().equals(user)) {
			invitedByUser = (invitedByUser == null ? authed_user : invitedByUser);
			Motd message = new Motd();
			message.setUser(user);
			message.setMessage("You have been added to friend pool '"+pool.getName()+"' by "+invitedByUser);
			message.setProcessed(0);
			session.save(message);
		}
		
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
		
		Query query = session.createQuery("select pm.pool.pool_id as pool_id, pm.pool.name as name, "+
							"pm.pool.num_players as num_players from PoolMember pm where pm.user.username = :username");
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
		
		Query query = session.createQuery("select pm.user.username as username, pm.user.facebook_id as facebook_id, "+
										  "pm.rank as rank, pm.winnings as winnings "+
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
	@SuppressWarnings("unchecked")
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
		
		
		// Remove user's entries from pool_game_leaderboard
		log.debug(username+" | Removing entries from pool_game_leaderboard for user "+username+" and pool "+pool_id);
		StringBuffer poolGameLeaderboardQueryString = new StringBuffer("from PoolGameLeaderboard pgl ");
		poolGameLeaderboardQueryString.append("where pgl.pool.pool_id = :pool_id ");
		poolGameLeaderboardQueryString.append("and pgl.user.username = :username");
		Query poolGameLeaderboardQuery = session.createQuery(poolGameLeaderboardQueryString.toString());
		poolGameLeaderboardQuery.setParameter("pool_id", pool_id);
		poolGameLeaderboardQuery.setParameter("username", username);
		
		List <PoolGameLeaderboard> pglList= poolGameLeaderboardQuery.list();
		for (PoolGameLeaderboard pgl: pglList) {
			log.debug(username+" | Removing pool_game_leaderboard entry for pool "+pool_id+" and game "+pgl.getGame().getGame_id());
			session.delete(pgl);
		}
		
		// Remove user's entries from pool_comp_leaderboard
		log.debug(username+" | Removing entries from pool_comp_leaderboard for user "+username+" and pool "+pool_id);
		StringBuffer poolCompLeaderboardQueryString = new StringBuffer("from PoolCompLeaderboard pcl ");
		poolCompLeaderboardQueryString.append("where pcl.pool.pool_id = :pool_id ");
		poolCompLeaderboardQueryString.append("and pcl.user.username = :username");
		Query poolCompLeaderboardQuery = session.createQuery(poolCompLeaderboardQueryString.toString());
		poolCompLeaderboardQuery.setParameter("pool_id", pool_id);
		poolCompLeaderboardQuery.setParameter("username", username);
		
		List <PoolCompLeaderboard> pclList= poolCompLeaderboardQuery.list();
		for (PoolCompLeaderboard pcl: pclList) {
			log.debug(username+" | Removing pool_comp_leaderboard entry for pool "+pool_id+" and comp "+pcl.getCompetition().getComp_id());
			session.delete(pcl);
		}
		
		// Get pool object
		Pool pool = poolMember.getPool();
		
		// Delete pool member then delete pool if this is last member
		if (pool.getNum_players() <= 1) {
			session.delete(poolMember);
			log.debug(username+" | Deleting pool "+pool_id+" as no more members remaining");
			session.delete(pool);
		} else {
			// Just remove user from pool
			session.delete(poolMember);
		}
		
		// Return null on success as client will just be looking at the HTTP response
		return null;
	}
	
	
	/*
	 * POST pool/update
	 */
	public Pool updatePool(Integer pool_id, String name) {
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		log.debug(username+" | Updating pool "+pool_id);
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		// Get pool object
		Pool pool = (Pool) session.get(Pool.class, pool_id);
		if (pool == null) {
			log.error(username+" | Pool with ID "+pool_id+" does not exist, cannot update pool");
			throw new InvalidParameterException(new RestError(3100, "Pool with ID "+pool_id+" does not exist, cannot update pool"));
		}
		
		// Only pool creator can update pool name
		if (pool.getCreated_by().getUsername().equals(username)) {
			if (!pool.getName().equals(name)) {
				pool.setName(name);
				session.save(pool);
				return pool;
			} else {
				log.debug(username+" | No changes made to pool");
				throw new InvalidStateException(new RestError(3102, "No changes made to pool"));
			}
		} else {
			log.error(username+" | Pools can only be updated by their creator");
			throw new InvalidStateException(new RestError(3101, "Pools can only be updated by their creator"));
		}
		
	}
	
	
	/*
	 * GET pool/points
	 */
	public PoolPointsResponse getPoolPoints(Integer pool_id, Integer game_id, Integer comp_id) {
		// Get username of player
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession(); 
		
		if (game_id != null) {
			log.debug(username+" | Getting pool points response for pool "+pool_id+" and game: "+game_id);
			
			StringBuffer queryString = new StringBuffer("SELECT ");
			queryString.append("pool_players.pool_size * g.stake as pool_pot_size, ");
			queryString.append("ge.total_points as points, ");
			queryString.append("pgl.rank as pool_rank, ");
			queryString.append("pgl.potential_winnings as pool_winnings, ");
			queryString.append("pool_players.pool_size as pool_size ");
			queryString.append("FROM ");
			queryString.append("pool_game_entry pge ");
			queryString.append("left join game_entry ge on ge.game_entry_id = pge.game_entry ");
			queryString.append("left join pool_member pm on pm.pool_member_id = pge.pool_member ");
			queryString.append("left join user u on u.user_id = pm.user ");
			queryString.append("left join game g on g.game_id = ge.game ");
			queryString.append("left join pool_game_leaderboard pgl on ");
			queryString.append("pgl.user = pm.user and pgl.game = ge.game and pgl.pool = pm.pool ");
			queryString.append("left join ");
			queryString.append("(select count(*) as pool_size, ");
			queryString.append("pgl.game as game_id ");
			queryString.append("from pool_game_leaderboard pgl ");
			queryString.append("where pgl.game = :game_id ");
			queryString.append("and pgl.pool = :pool_id ");
			queryString.append(") as pool_players ");
			queryString.append("on pool_players.game_id = ge.game ");
			queryString.append("WHERE ");
			queryString.append("pm.pool = :pool_id ");
			queryString.append("and ge.game = :game_id ");
			queryString.append("and u.username = :username ");
			
			SQLQuery sqlQuery = session.createSQLQuery(queryString.toString());
			sqlQuery.setParameter("pool_id", pool_id);
			sqlQuery.setParameter("game_id", game_id);
			sqlQuery.setParameter("username", username);
			sqlQuery.addScalar("pool_pot_size", org.hibernate.type.IntegerType.INSTANCE);
			sqlQuery.addScalar("points",  org.hibernate.type.IntegerType.INSTANCE);
			sqlQuery.addScalar("pool_rank",  org.hibernate.type.IntegerType.INSTANCE);
			sqlQuery.addScalar("pool_winnings",  org.hibernate.type.IntegerType.INSTANCE);
			sqlQuery.addScalar("pool_size",  org.hibernate.type.IntegerType.INSTANCE);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(PoolPointsResponse.class));
			
			return (PoolPointsResponse) sqlQuery.uniqueResult();
			
		} else if (comp_id != null) {
			log.debug(username+" | Getting pool points response for pool "+pool_id+" and comp: "+game_id);
			
			StringBuffer queryString = new StringBuffer("SELECT ");
			queryString.append("pcl.rank as pool_rank, ");
			queryString.append("pcl.winnings as pool_winnings, ");
			queryString.append("pool_comp_stats.pool_size as pool_size, ");
			queryString.append("pool_comp_stats.total_pool_winnings as total_pool_winnings ");
			queryString.append("FROM ");
			queryString.append("pool_comp_leaderboard pcl ");
			queryString.append("LEFT JOIN user u ON u.user_id = pcl.user ");
			queryString.append("LEFT JOIN ");
			queryString.append("( SELECT ");
			queryString.append("COUNT(*) as pool_size, ");
			queryString.append("SUM(pcl.winnings) AS total_pool_winnings, ");
			queryString.append("pcl.competition as competition ");
			queryString.append("FROM ");
			queryString.append("pool_comp_leaderboard pcl ");
			queryString.append("WHERE ");
			queryString.append("pcl.competition = :comp_id AND ");
			queryString.append("pcl.pool = :pool_id ");
			queryString.append(") AS pool_comp_stats ");
			queryString.append("on 	pool_comp_stats.competition = pcl.competition ");
			queryString.append("WHERE ");
			queryString.append("pcl.competition = :comp_id AND ");
			queryString.append("pcl.pool = :pool_id AND ");
			queryString.append("u.username = :username ");
			
			SQLQuery sqlQuery = session.createSQLQuery(queryString.toString());
			sqlQuery.setParameter("pool_id", pool_id);
			sqlQuery.setParameter("comp_id", comp_id);
			sqlQuery.setParameter("username", username);
			sqlQuery.addScalar("pool_rank",  org.hibernate.type.IntegerType.INSTANCE);
			sqlQuery.addScalar("pool_winnings",  org.hibernate.type.IntegerType.INSTANCE);
			sqlQuery.addScalar("pool_size",  org.hibernate.type.IntegerType.INSTANCE);
			sqlQuery.addScalar("total_pool_winnings",  org.hibernate.type.IntegerType.INSTANCE);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(PoolPointsResponse.class));
			
			return (PoolPointsResponse) sqlQuery.uniqueResult();
		} 

		return null;
	}
	
}
