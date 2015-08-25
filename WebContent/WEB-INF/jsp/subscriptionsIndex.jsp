<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

	<!-- Import style sheets -->
	<link rel="stylesheet" type="text/css" href="css/subscriptionsIndex.css">
	
	<link rel="stylesheet" type="text/css" href="css/commonUIElements.css">
	
	<!-- Import javascripts -->
	<script src="js/jQuery/jquery-1.10.2.min.js"></script>
	
	<script src="js/subscriptionsIndex.js"></script>
	
	<script src="js/utils.js"></script>
	<script src="js/spinner/spin.min.js"></script>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Subscriptions Manager</title>
	</head>

	<body id="bodyID">
		
		<div id="welcome">Manager | Manage Subscriptions here!</div>
		
		<div id="addSubscriptions" class="button" onclick="addSubscriptionsClicked(event)">Add Subscriptions</div>
		
		<div id="listSubscriptions" class="button" onclick="listSubscriptionsClicked(event)">List Subscriptions</div>
		
		<div id="publishChangeInSubscriptionsTable" class="button" onclick="publishChangesClicked(event)">Publish Changes Made in Subscriptions Table</div>
	</body>
</html>