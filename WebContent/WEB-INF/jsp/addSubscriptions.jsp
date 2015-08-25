<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

	<!-- Import style sheets -->
	<link rel="stylesheet" type="text/css" href="css/addSubscriptions.css">
	
	<link rel="stylesheet" type="text/css" href="css/commonUIElements.css">
	
	<!-- Import javascripts -->
	<script src="js/jQuery/jquery-1.10.2.min.js"></script>
	
	<script src="js/addSubscriptions.js"></script>
	
	<script src="js/spinner/spin.min.js"></script>
	
	<script src="js/utils.js"></script>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

		<title>Manager | Add a subscription!</title>
	</head>

	<body id="bodyID">
	
		<div id="welcome">Add Subscriptions</div>
	
		<table style="width:100%; margin-top:80px;">
			
			<tr>
				
				<td>
					<p>Name</p>
					<input id="name"/>
				</td>
				
				<td>
					<p>Price</p>
					<input type="number" id="price"/>
				</td>
				
				<td>
					<p>Caution Money</p>
					<input type="number" id="security"/>
				</td>
				
			</tr>
			
			<tr>
				
				<td>
					<p>Number of Books</p>
					<input type="number" id="books"/>
				</td>
				
				<td>
					<p>Number of Deliveries</p>
					<input type="number" id="deliveries"/>
				</td>
				
				<td>
					<p>Region For</p>
					<select id="region">
  						<option value="Delhi/NCR">Delhi/NCR</option>
  						<option value="Bangalore">Bangalore</option>
  						<option value="Mumbai">Mumbai</option>
  						<option value="Hyderabad">Hyderabad</option>
					</select>
				</td>
			</tr>
			
			<tr>
				
				<td>
					<p>Description</p>
					<textarea rows="12" cols="70" id="desc"></textarea>
				</td>
				
				<td>
					<p>Duration in days</p>
					<input type="number" id="duration"/>
				</td>
			</tr>
		</table>
		
		<div id="submit" class="button" onClick="submitSubscription(event)">Submit Subscription</div>

	</body>
	
	<div id="loadingOverlay" class="progressoverlay"></div>
	
	<div id="loadingSpinner"></div>
</html>