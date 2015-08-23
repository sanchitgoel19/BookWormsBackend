var spinner = null;

var spinnerBeingShown = false;

function showLoadingProgress()
{
	spinnerBeingShown = true;
	
	$("#loadingOverlay").css("display", "block");
	$("#loadingSpinner").css("display", "block");
	$("#loadingOverlay").fadeIn("slow");
	
	if(spinner == null)
		spinner = new Spinner().spin();
	
	spinner.spin(document.getElementById('loadingSpinner'));	
		
}

function hideLoadingProgress()
{
	spinnerBeingShown = false;
	
	$("#loadingOverlay").css("display", "none");
	$("#loadingSpinner").css("display", "none");
	$("#loadingOverlay").hide();
	spinner.stop();
}