package com.bookwrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
@Import(HibernateConfig.class)
class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public ViewResolver getViewResolver()
	{
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setCache(true);
		
		//If referred any .jsp file pick it up from /WEB-INF/jsp/
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		
		return resolver;
		
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		
		//If referred views/Assets or css or javascript...pick it up from /WEB-INF/***
		//Keep in mind that URL referring css/js/assets should be of the form Hostname/BookWrms/views/***
		
		registry.addResourceHandler("/views/Manager/Assets/**").addResourceLocations("/WEB-INF/Assets/");
        registry.addResourceHandler("/views/Manager/css/**").addResourceLocations("/WEB-INF/css/");
        registry.addResourceHandler("/views/Manager/js/**").addResourceLocations("/WEB-INF/js/");
	}
	
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
