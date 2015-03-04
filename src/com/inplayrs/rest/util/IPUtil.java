package com.inplayrs.rest.util;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.inplayrs.rest.constants.URL;

public class IPUtil {

	@Resource(name="sessionFactory")
	private static SessionFactory sessionFactory;
	
	public static void setSessionFactory(SessionFactory sessionFactory) {
		IPUtil.sessionFactory = sessionFactory;
	}
	
	public static String listToCommaSeparatedString(List<String> stringList) {
		if (stringList != null && !stringList.isEmpty()) {
			StringBuffer commaSeparatedString = new StringBuffer();
			for (String s : stringList) {
				if (commaSeparatedString.length() > 0) {
					commaSeparatedString.append(", ").append(s);
				} else {
					commaSeparatedString.append(s);
				}
			}
			return commaSeparatedString.toString();
		} else {
			return null;
		}
	}
	
	
	/*
	 * Checks if given word is in list of banned words
	 */
	public static boolean isBadWord(String word) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
				
		// Verify whether the requested username is in the list of bad words
		Query badWordQuery = session.createQuery("select count(*) from BadWord where word = :badword");
		badWordQuery.setParameter("badword", word.toLowerCase());
		
		if (( (Long) badWordQuery.iterate().next() ).intValue() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Generates key to be used to store photos to S3 with in photo game
	 */
	public static String generatePhotoKey(int game_id, String username) {
		StringBuilder photoKey = new StringBuilder(URL.AWS_PHOTO_URL);
		return photoKey.append('/').append(game_id).append('/').append(username).append(':').append(System.nanoTime()).toString();
	}
	
}
