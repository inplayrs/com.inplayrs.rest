package com.inplayrs.rest.service;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inplayrs.rest.ds.TestTable;



/*
 * test service
 */
@Service("testService")
@Transactional
public class TestService {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	/*
	 * Test function - return one record from table
	 */
	public TestTable getTestTable(int id) {
	    // Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Retrieve existing period first
		TestTable testTable = (TestTable) session.get(TestTable.class, id);
		   
		return testTable;	
	}
	
	
	/**
	  * Test function - return all records from test table
	  */
	@SuppressWarnings("unchecked")
	public List<TestTable> getTestTables() {
	   
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM TestTable t");
		   
		// Retrieve all
		return  query.list();
	 }
	
	
	/**
	  * Test function - add a new record in test table
	  */
	public Integer addTestTable(TestTable testTable) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		   
		// Save
		return (Integer)session.save(testTable);
	}
	
	
	
}
