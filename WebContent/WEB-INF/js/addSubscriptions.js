function submitSubscription(event)
{
	addSubscription();
}

function getName()
{
	var name = document.getElementById("name");
	return name.value;
}

function validateDecimals(n)
{
	return !isNaN(parseFloat(n)) && isFinite(n);
}

function validateInts(n)
{
	return n.match(/^[0-9]*$/) !== null;
}

function getPrice()
{
	var price = document.getElementById("price");
	return price.value;
}

function getSecurity()
{
	var security = document.getElementById("security");
	return security.value;
}

function getNumberBooks()
{
	var numberBooks = document.getElementById("books");
	return numberBooks.value;
}

function getDeliveries()
{
	var deliveries = document.getElementById("deliveries");
	return deliveries.value;
}

function getRegion()
{
	var regionList = document.getElementById("region");
	return regionList.options[regionList.selectedIndex].value;
}

function getDescription()
{
	var desc = document.getElementById("desc");
	return desc.value;
}

function getDuration()
{
	var duration = document.getElementById("duration");
	return duration.value;
}

function addSubscription()
{
	
	var name = getName();
	if(name === null || name === "" || typeof name === undefined){
		alert("Please enter a name for subscription");
		return;
	}
	
	var price = getPrice();
	if(!price.length || (price.length && !validateDecimals(price))){
		alert("Please enter a valid price");
		return;
	}
	price = parseFloat(price);
	
	var security = getSecurity();
	if(!security.length || (security.length && !validateDecimals(security))){
		alert("Please enter a valid security amount");
		return;
	}
	security = parseFloat(security);
	
	var numberBooks = getNumberBooks();
	if(!numberBooks.length || (numberBooks.length && !validateInts(numberBooks))){
		alert("Please enter valid number of books");
		return;
	}
	numberBooks = parseInt(numberBooks);
	
	var numberDeliveries = getDeliveries();
	if(!numberDeliveries.length || (numberDeliveries.length && !validateInts(numberDeliveries))){
		alert("Please enter valid number of deliveries");
		return;
	}
	numberDeliveries = parseInt(numberDeliveries);

	var region = getRegion();
	
	var description = getDescription();
	if(description === null || description === "" || typeof description === undefined){
		alert("Please enter a description for subscription");
		return;
	}
	
	var duration = getDuration();
	if(!duration.length || (duration.length && !validateInts(duration))){
		alert("Please enter valid duration");
		return;
	}
	duration = parseInt(duration);
	
	var location = document.createElement('a');
	location.href = document.URL;
    var URL = location.origin;
    
    
    URL = URL + "/BookWrms/api/subscriptions/add";
    
    showLoadingProgress();
    
    var subscriptionProps = {
    		name:name,
    		price:price,
    		security:security,
    		numberBooks:numberBooks,
    		numberDeliveries:numberDeliveries,
    		region:region,
    		description:description,
    		duration:duration
    };
    
    $.ajax({
        url: URL,
        type: 'PUT',  
        data: JSON.stringify(subscriptionProps),
        contentType: 'application/json; charset=utf-8',
        success: function(result) {
        	hideLoadingProgress();
        	alert("Subscription added successfully");
        	
            window.location.reload(false);
        },
        error: function(status, textStatus){
    		hideLoadingProgress();
    		alert('Request failed. Please check your internet connection and try again.');
           }
    });  
}