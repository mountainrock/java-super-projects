DEBUG = false;
	$(document).ready(function (){
	  	$("#alertChkBox").click(function(event){
	  	  if($(this).attr('checked')){
		    $("#emailFormatTR,#frequencyTR").show();
		   }
		   else{
		   	$("#emailFormatTR,#frequencyTR").hide();
		   }
 	  });
 	  
 	  $("#emailFormatTR,#frequencyTR").hide();
	});
	
