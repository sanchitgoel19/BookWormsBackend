package com.bookwrms.controller.rest;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class BaseController {
	
	protected SessionFactory sessionFactory;
	protected SessionFactory sessionFactoryStage;
	
	@Autowired
	protected void setSessionFactory(@Qualifier("sessionFactory") SessionFactory factory)
	{
		sessionFactory = factory;
	}
	
	@Autowired
	protected void setSessionFactoryStage(@Qualifier("sessionFactoryStage") SessionFactory factory)
	{
		sessionFactoryStage = factory;
	}

	protected SessionFactory getSessionFactory(String stage)
	{
		if(stage == null || stage == "false")
		{
			return sessionFactory;
		}
		return sessionFactoryStage;
	}
}
