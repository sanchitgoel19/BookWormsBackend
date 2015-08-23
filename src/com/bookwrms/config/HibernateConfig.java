package com.bookwrms.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@Configuration
public class HibernateConfig {
	
	private String driverClassName = "com.mysql.jdbc.Driver";
	
	private String username = "root";
    private String password = "";
    
    private String hibernateShowSql = "true";
    private String hibernateHbm2ddlAuto = "validate";
    
    private String hibernateDialect = "com.bookwrms.dialect.BookWrmsSQLDialect";
    
    //FIXME::RDS URL is hard-coded to localhost:3306 for local database currently
    
    @Bean
    public DataSource getDataSource()
    {
    	//get DataSource for Production DataBase
    	String url = "jdbc:mysql://" + "localhost:3306" + "/bookwrms?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
    	
    	DriverManagerDataSource ds = new DriverManagerDataSource();
    	ds.setDriverClassName(driverClassName);
    	ds.setUrl(url);
    	ds.setUsername(username);
    	ds.setPassword(password);
    	
    	return ds;
    }

    @Bean
    public DataSource getDataSourceStage()
    {
    	//get DataSource for stage Database
    	String url = "jdbc:mysql://" + "localhost:3306" + "/bookwrms_stage?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
    	
    	DriverManagerDataSource ds = new DriverManagerDataSource();
    	ds.setDriverClassName(driverClassName);
    	ds.setUrl(url);
    	ds.setUsername(username);
    	ds.setPassword(password);
    	
    	return ds;
    }
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
    {
    	HibernateTransactionManager htm = new HibernateTransactionManager();
    	htm.setSessionFactory(sessionFactory);
		
    	return htm;
    }
    
    @Bean
    @Autowired
    public HibernateTemplate getHibernateTemplate(SessionFactory sessionFactory)
    {
    	HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
        return hibernateTemplate;
    }

	private Properties getHibernateProperties() {
		
		Properties properties = new Properties();
		properties.put("hibernate.dialect", hibernateDialect);
		properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        properties.put("hibernate.connection.CharSet", "utf8");
        properties.put("hibernate.connection.characterEncoding", "utf8");
        properties.put("hibernate.connection.useUnicode", "true");
        
		return properties;
	}
	
	@Bean
    public LocalSessionFactoryBean sessionFactory()
    {
		//get Production Session Factory
    	LocalSessionFactoryBean asfb = new LocalSessionFactoryBean();
    	asfb.setDataSource(getDataSource());
    	asfb.setHibernateProperties(getHibernateProperties());
    	asfb.setPackagesToScan(new String[]{"com.bookwrms.model.prod"});
    	
    	return asfb;
    }
	
	@Bean
	public LocalSessionFactoryBean sessionFactoryStage()
	{
		//get Stage session factory
		LocalSessionFactoryBean asfb = new LocalSessionFactoryBean();
		asfb.setDataSource(getDataSourceStage());
		asfb.setHibernateProperties(getHibernateProperties());
		asfb.setPackagesToScan(new String[]{"com.bookwrms.model.prod","com.bookwrms.model.stage"});
		
		return asfb;
	}
}
