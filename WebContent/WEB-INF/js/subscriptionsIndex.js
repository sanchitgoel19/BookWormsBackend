function addSubscriptionsClicked(event)
{
	window.open("addSubscriptions", "_self");
}

function listSubscriptionsClicked(event)
{
	window.open("listSubscriptions", "_self");
}

function publishChangesClicked(event)
{
	var r = window.confirm("It will publish from stage to prod the subscriptions. Are you sure to continue? Have you verified on stage?");
	
	if(r == true){
		var location = document.createElement('a');
		location.href = document.URL;
		var URL = location.origin;
    
		URL += "/BookWrms/api/subscriptions/publish";
    
		showLoadingProgress();
		
		var element = this;
	
		$.ajax({
			type: "POST",
			url: URL,
			success: function(response, textStatus ){
			
				alert('Subscriptions published Successfully');
			},
			error: function(status, textStatus){
        	
				hideLoadingProgress();
				alert('Request failed. Please check your internet connection and try again.');
        	
			}
		});
	}
}