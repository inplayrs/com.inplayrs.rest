package com.inplayrs.rest.service;

import java.util.List;
import javax.annotation.Resource;
//import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.ds.Competition;


/*
 * Service for processing game data
 */
@Service("competitionService")
@Transactional
public class CompetitionService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	public List<Competition> getCompetitions(int cat_id) {
			   
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM Competition c where c.category = ".concat(Integer.toString(cat_id)));
		   
		// Retrieve all
		return  query.list();
	 }
	
	
}
