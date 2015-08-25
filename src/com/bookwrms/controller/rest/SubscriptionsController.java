package com.bookwrms.controller.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookwrms.model.prod.Subscriptions;
import com.bookwrms.model.stage.DeletedSubscriptions;
import com.bookwrms.model.stage.NewAddedSubscriptions;
import com.bookwrms.utils.AppUtils;

@RestController
public class SubscriptionsController extends BaseController {
	
	public static class SubscriptionsProperties{
		
		public String name = null;
		public BigDecimal price = BigDecimal.ZERO;
		public BigDecimal security = BigDecimal.ZERO;
		public Long numberBooks = new Long(0);
		public Long numberDeliveries = new Long(0);
		public String region = null;
		public String description = null;
		public Long duration = new Long(0);
	}

	
	static public class SubscriptionListResult extends Subscriptions{
		public SubscriptionListResult(Subscriptions card){
			super(card);
		}
		
		public String status = "published";	
	}
	
	@RequestMapping(value = "/api/subscriptions/list" , method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Subscriptions>> listSubscriptions(@RequestParam(required = false) final String stage,
			@RequestParam(required = false) final String region){
				Session session = getSessionFactory(stage).openSession();
				
				try{
					List<Subscriptions> subscriptions = getSubscriptionsForRegion(session, region);
					
					return new ResponseEntity<List<Subscriptions>>(subscriptions, HttpStatus.OK);
					}catch(Exception e){
					e.printStackTrace();
					return new ResponseEntity<List<Subscriptions>>(HttpStatus.BAD_REQUEST);
					}finally{
					AppUtils.finishSession(session);
					}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "api/subscriptions/listall", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<SubscriptionListResult>> listAllSubscriptions(@RequestParam(required = false) final String region){
		
		Session session = getSessionFactory("true").openSession();
		
		try{
			List<Subscriptions> subscriptions = getSubscriptionsForRegion(session, region);
			
			Query query = null;
			
			String queryString;
			
			queryString = "Select id From NewAddedSubscriptions";
			
			query = session.createQuery(queryString);
			
			List<String> newAddedSubscriptions = query.list();
			
			List<SubscriptionListResult> subscriptionListResult = new ArrayList<SubscriptionListResult>();
			
			for(Subscriptions subscription : subscriptions){
				
				SubscriptionListResult result = new SubscriptionListResult(subscription);
				
				result.status = newAddedSubscriptions.contains(subscription.getId()) ? "unpublished" : "published";
				
				subscriptionListResult.add(result);
			}
			
			return new ResponseEntity<List<SubscriptionListResult>>(subscriptionListResult, HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<List<SubscriptionListResult>>(HttpStatus.BAD_REQUEST);
		}finally{
			AppUtils.finishSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Subscriptions> getSubscriptionsForRegion(Session session,
			String region) {
		
		Query query = null;
		
		String queryString;
		
		if(region != null)
			queryString = "From Subscriptions where region is :region";
		else
			queryString = "From Subscriptions";
		
		query = session.createQuery(queryString);
		
		if(region != null)
			query.setString("region", region);
		
		List<Subscriptions> subscriptions = query.list();
			
		return subscriptions;
	}
	
	
	@RequestMapping(value = "/api/subscriptions/add" , method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Void> addNewSubscription(@RequestBody SubscriptionsProperties props){
		if(addOrModifySubscriptionsHelper(props, null, false)){
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		else{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	

	@RequestMapping(value = "/api/subscriptions/delete/{subscriptionID}", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteSubscription(@PathVariable String subscriptionID) {

		if(deleteSubscriptionsHelper(subscriptionID)){
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		else{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean deleteSubscriptionsHelper(String subscriptionID) {
		
		if(subscriptionID == null)
			return false;
		else{
			Session session = getSessionFactory("true").openSession();
			
			Transaction deleteSubscriptionTransaction = session.beginTransaction();
			
			try{
				
				Query query = null;
				
				String queryString = "From NewAddedSubscriptions where id is :subscriptionID";
				
				query = session.createQuery(queryString);
				
				query.setString("subscriptionID", subscriptionID);
				
				List<NewAddedSubscriptions> newAddedSubscriptionsWithThisID = query.list();
				
				if(newAddedSubscriptionsWithThisID.size() == 0){
				
					DeletedSubscriptions deletedSubscriptions = new DeletedSubscriptions(subscriptionID);
				
					session.saveOrUpdate(deletedSubscriptions);
					
				}else{
					
					for(NewAddedSubscriptions newAddedSubscription : newAddedSubscriptionsWithThisID)
						
						session.delete(newAddedSubscription);
				}
				
				queryString =  "From Subscriptions where id is :subscriptionID";
				
				query = session.createQuery(queryString);
				
				query.setString("subscriptionID", subscriptionID);
				
				List<Subscriptions> subscription = query.list();
				
				for(Subscriptions obj : subscription)
					session.delete(obj);
				
				deleteSubscriptionTransaction.commit();
				
				return true;
			}
			catch(Exception e){
				
				e.printStackTrace();
				deleteSubscriptionTransaction.rollback();
				return false;
			}
			finally{
				AppUtils.finishSession(session);
			}
		}
	}

	private boolean addOrModifySubscriptionsHelper(SubscriptionsProperties props, String inSubscriptionID,
			boolean isSubscriptionBeingModified){
		String subscriptionID;
		if(inSubscriptionID == null){
			subscriptionID = AppUtils.generateUUID();
		}
		else{
			subscriptionID =inSubscriptionID;
		}
		
		Session session = getSessionFactory("true").openSession();
		
		try{
			
			Transaction addOrModifyTransaction = session.beginTransaction();
			
			
			Subscriptions subscription = new Subscriptions(subscriptionID, props.name, props.price, props.security,
					props.numberBooks, props.numberDeliveries, props.description, props.region, props.duration);
				
			try{
				session.saveOrUpdate(subscription);
					
				NewAddedSubscriptions newAddedSubscription = new NewAddedSubscriptions(subscriptionID);
					
				session.saveOrUpdate(newAddedSubscription);
					
				addOrModifyTransaction.commit();
					
				return true;
			}
			catch(Exception e){
					
				e.printStackTrace();
				addOrModifyTransaction.rollback();
				return false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			if(session != null)
				AppUtils.finishSession(session);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/subscriptions/publish" , method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Void> publishSubscriptions(){
		
		Session sessionStage = getSessionFactory("true").openSession();
		
		Session sessionProd = getSessionFactory("false").openSession();
		
		try{
			
			Transaction publishTransactionStage = sessionStage.beginTransaction();
			
			Transaction publishTransactionProd = sessionProd.beginTransaction(); 
			
			try{
				
				Query query = null;
				
				String queryString = "From NewAddedSubscriptions";
				
				query = sessionStage.createQuery(queryString);
				
				List<NewAddedSubscriptions> newAddedSubscriptionsIDs = query.list();
				
				for(NewAddedSubscriptions newAddedSubscriptions : newAddedSubscriptionsIDs){
					
					queryString = "From Subscriptions where id is :subscriptionID";
					
					query = sessionStage.createQuery(queryString);
					
					query.setString("subscriptionID", newAddedSubscriptions.getId());
					
					List<Subscriptions> newAddedSubscriptionWithID = query.list();
					
					for(Subscriptions subscription : newAddedSubscriptionWithID){
						sessionProd.saveOrUpdate(subscription);
					}
					
					sessionStage.delete(newAddedSubscriptions);
				}
				
				queryString = "From DeletedSubscriptions";
				
				query = sessionStage.createQuery(queryString);
				
				List<DeletedSubscriptions> newDeletedSubscriptionsIDs = query.list();
								
				for(DeletedSubscriptions deletedSubscription : newDeletedSubscriptionsIDs){
					
					queryString = "From Subscriptions where id is :subscriptionID";
					
					query = sessionProd.createQuery(queryString);
					
					query.setString("subscriptionID", deletedSubscription.getId());
					
					List<Subscriptions> newDeletedSubscriptions = query.list();
					
					for(Subscriptions subscription : newDeletedSubscriptions)
						sessionProd.delete(subscription);
					
					sessionStage.delete(deletedSubscription);
				}
				
				publishTransactionStage.commit();
				publishTransactionProd.commit();
				
				return new ResponseEntity<Void>(HttpStatus.OK);
				
			}catch(Exception e){
				e.printStackTrace();
				publishTransactionStage.rollback();
				publishTransactionProd.rollback();
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<Void> (HttpStatus.BAD_REQUEST);
		}finally{
			AppUtils.finishSession(sessionStage);
			AppUtils.finishSession(sessionProd);
		}
		
		
	}
	
}
