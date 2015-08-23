package com.bookwrms.controller.rest;

import java.math.BigDecimal;
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
import com.bookwrms.model.stage.BackupSubscriptions;
import com.bookwrms.model.stage.DeletedSubscriptions;
import com.bookwrms.model.stage.ModifiedSubscriptions;
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

	}

	@RequestMapping(value = "/api/subscriptions/listall" , method = RequestMethod.GET, produces = "application/json")
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
	
	@RequestMapping(value = "/api/subscriptions/modify/{subscriptionID}", method = RequestMethod.PUT, produces="application/json")
    public ResponseEntity<Void> modifySubscription(@PathVariable String subscriptionID, @RequestBody SubscriptionsProperties props) {

		if(addOrModifySubscriptionsHelper(props, subscriptionID, true)){
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		else{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	

	@RequestMapping(value = "/api/subscriptions/delete/{subscriptionID}", method = RequestMethod.PUT, produces="application/json")
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
			Session session = sessionFactoryStage.openSession();
			
			Transaction deleteSubscriptionTransaction = session.beginTransaction();
			
			try{
				
				Query query = null;
				
				String queryString =  "From Subscriptions where id:subscriptionID";
				
				query = session.createQuery(queryString);
				
				query.setString("subscriptionID", subscriptionID);
				
				List<Subscriptions> subscription = query.list();
				
				BackupSubscriptions backupSubscriptions = new BackupSubscriptions(subscription.get(0).getId(),
						subscription.get(0).getName(), subscription.get(0).getPrice(), subscription.get(0).getSecurity(),
						subscription.get(0).getNumberBooks(), subscription.get(0).getDelieveries(), subscription.get(0).getDescription(),
						subscription.get(0).getRegion());
				
				session.saveOrUpdate(backupSubscriptions);
				
				DeletedSubscriptions deletedSubscriptions = new DeletedSubscriptions(subscriptionID);
				
				session.saveOrUpdate(deletedSubscriptions);
				
				session.delete(subscription);
				
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

	@SuppressWarnings("unchecked")
	private boolean addOrModifySubscriptionsHelper(SubscriptionsProperties props, String inSubscriptionID,
			boolean isSubscriptionBeingModified){
		String subscriptionID;
		if(inSubscriptionID == null){
			subscriptionID = AppUtils.generateUUID();
		}
		else{
			subscriptionID =inSubscriptionID;
		}
		
		Session session = sessionFactoryStage.openSession();
		
		try{
			
			Transaction addOrModifyTransaction = session.beginTransaction();
			
			if(isSubscriptionBeingModified){
				
				Query query = null;
				
				try{
					String queryString = "From Subscriptions where id :subscriptionid";
				
					query = session.createQuery(queryString);
				
					query.setString("subscriptionid", subscriptionID);
				
					List<Subscriptions> subscription = query.list();
				
					BackupSubscriptions backupSubscriptions = new BackupSubscriptions(subscription.get(0).getId(),
							subscription.get(0).getName(), subscription.get(0).getPrice(), subscription.get(0).getSecurity(),
							subscription.get(0).getNumberBooks(), subscription.get(0).getDelieveries(), subscription.get(0).getDescription(),
							subscription.get(0).getRegion());
					
					session.saveOrUpdate(backupSubscriptions);
					
					subscription.get(0).setName(props.name);
					subscription.get(0).setPrice(props.price);
					subscription.get(0).setSecurity(props.security);
					subscription.get(0).setNumberBooks(props.numberBooks);
					subscription.get(0).setDelieveries(props.numberDeliveries);
					subscription.get(0).setRegion(props.region);
					subscription.get(0).setDescription(props.description);
				
					session.update(subscription);
					
					ModifiedSubscriptions modifiedSubscriptions = new ModifiedSubscriptions(subscriptionID);
					
					session.saveOrUpdate(modifiedSubscriptions);
					
					addOrModifyTransaction.commit();
					
					return true;
				}
				catch(Exception e){
					
					e.printStackTrace();
					addOrModifyTransaction.rollback();
					return false;
					
				}
			}
			else{
				Subscriptions subscription = new Subscriptions(subscriptionID, props.name, props.price, props.security,
						props.numberBooks, props.numberDeliveries, props.description, props.region);
				
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
}
