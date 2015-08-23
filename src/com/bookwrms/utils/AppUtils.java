package com.bookwrms.utils;

import java.util.UUID;

import org.hibernate.Session;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AppUtils implements ApplicationContextAware {

	private static ApplicationContext ctx;
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		
		ctx = context;

	}

	
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}
	
	public static void finishSession(Session session){
		session.flush();
		session.clear();
		session.close();
	}
	
	
	//Some useful helper String functions
	public static boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}

		if (s.length() < 1) {
			return true;
		}

		return false;
	}
	
	public static boolean isBlank(String s) {
		if (isEmpty(s)) {
			return true;
		}

		if (isEmpty(s.trim())) {
			return true;
		}

		return false;
	}
	
	
	//A helper function to generate random ID if required
	public static String generateUUID()	{
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		return uuid;
	}
	
	
	
	//A helper function to jsonify Java Objects using Jackson
	//This is used by controllers to jsonify Java Objects before sending response
	public static String jsonify(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
			throw (RuntimeException) e;
		}
	}
	
	
	static ObjectMapper objectMapper = new ObjectMapper();
}
