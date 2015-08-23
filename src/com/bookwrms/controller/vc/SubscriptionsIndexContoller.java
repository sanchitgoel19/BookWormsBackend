package com.bookwrms.controller.vc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SubscriptionsIndexContoller {

	@RequestMapping(value = "/views/Manager/subscriptionsIndex" , method = RequestMethod.GET)
	public ModelAndView subscriptionsIndex() {
		
		return new ModelAndView("subscriptionsIndex");
		
	}
	
	@RequestMapping(value = "/views/Manager/addSubscriptions", method = RequestMethod.GET)
	public ModelAndView addSubscriptions() {
		
		return new ModelAndView("addSubscriptions");
	}
	
	@RequestMapping(value = "/views/Manager/listSubscriptions", method = RequestMethod.GET)
	public ModelAndView listSubscriptions() {
		
		return new ModelAndView("listSubscriptions");
	}
}
