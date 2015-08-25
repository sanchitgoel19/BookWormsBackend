$(document).ready(function (e){
	
	var regionList = document.getElementById("region");
	
	listCardsForRegion(regionList.options[regionList.selectedIndex].value);
	
});


function createTableRow(subscription) {
	
	var newtr = document.createElement('tr');
	
	var newtd = document.createElement('td');
	
	newtd.innerHTML = subscription.name;
	newtd.className = 'scroll';
	
	newtr.appendChild(newtd);
	
	newtd = document.createElement('td'); 
	newtd.innerHTML = subscription.price;
	
	newtr.appendChild(newtd);
	
	newtd = document.createElement('td'); 
	newtd.innerHTML = subscription.security;
	
	newtr.appendChild(newtd);
	
	newtd = document.createElement('td'); 
	newtd.innerHTML = subscription.numberBooks;
	
	newtr.appendChild(newtd);
	
	newtd = document.createElement('td'); 
	newtd.innerHTML = subscription.delieveries;
	
	newtr.appendChild(newtd);
	
	newtd = document.createElement('td'); 
	newtd.innerHTML = subscription.region;
	
	newtr.appendChild(newtd);
	
	newtd = document.createElement('td'); 
	newtd.innerHTML = subscription.description;
	newtd.className = 'scroll';
	
	newtr.appendChild(newtd);
	
	newtd = document.createElement('td'); 
	newtd.innerHTML = subscription.duration;
	
	newtr.appendChild(newtd);
	
	newtd = document.createElement('td'); 
	newtd.innerHTML = subscription.status;
	
	newtr.appendChild(newtd);
	
	newtd = document.createElement('td');
	var deleteButton = document.createElement('div');
	deleteButton.className = 'button';
	var t = document.createTextNode("Delete");
	deleteButton.appendChild(t); 
	newtd.appendChild(deleteButton);
	
	newtr.appendChild(newtd);
	
	deleteButton.onclick= function()
	{
		var r = window.confirm("It will delete the card. Are you sure to continue?");
		
		if(r == true){
			var location = document.createElement('a');
			location.href = document.URL;
			var URL = location.origin;
	    
			URL += "/BookWrms/api/subscriptions/delete/";
			URL += subscription.id;
	    
			showLoadingProgress();
		
			$.ajax({
				type: "POST",
				url: URL,
				success: function(response, textStatus ){
					var row = this.parentNode.parentNode;
					document.getElementById("tablebody").deleteRow(row.rowIndex);
					hideLoadingProgress();
				
					alert('Card Deleted Successfully');
				},
				error: function(status, textStatus){
	        	
					hideLoadingProgress();
					alert('Request failed. Please check your internet connection and try again.');
	        	
				}
			});
		}
	};
	
	document.getElementById('tablebody').appendChild(newtr);
}

function listCardsForRegion(region) {
	
	showLoadingProgress();
	
	var location = document.createElement('a');
	location.href = document.URL;
    var URL = location.origin;
    
    URL += "/BookWrms/api/subscriptions/listall";
	
	$.ajax({
        type: "GET",
        url: URL,
        data:{stage:"true",region:region},
        success: function(response, textStatus ){
		        	
        	var table = document.getElementById("tablebody");
		        	
		    for(var i = table.rows.length - 1; i > 0; i--){
		    	
		        table.deleteRow(i);
		       
		    }
		    
		    for(var subscription in response){
		    	createTableRow(response[subscription]);
		    }
        			
        	hideLoadingProgress();
        },
        error: function(status, textStatus){
        	
        	hideLoadingProgress();
        	alert('Request failed. Please check your internet connection and try again.');
        	
        }
	});
}